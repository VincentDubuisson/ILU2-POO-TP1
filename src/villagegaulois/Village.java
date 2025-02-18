package villagegaulois;

import personnages.Chef;
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;

	public Village(String nom, int nbVillageoisMaximum, int nbEtalMarche) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		marche = new Marche(nbEtalMarche);
	}

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() {
		StringBuilder chaine = new StringBuilder();
		
		if (chef == null) {
			throw new VillageSansChefException("le village doit avoir un chef");
		}
		
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef "
					+ chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom()
					+ " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}
	
	public String installerVendeur(Gaulois vendeur, String produit, int nbProduit) {
		StringBuilder chaine = new StringBuilder();
		chaine.append(vendeur.getNom() + " cherche un endroit pour vendre " + nbProduit + " " + produit + ".");
		
		Etal etal = new Etal();
		etal.occuperEtal(vendeur, produit, nbProduit);
		int indiceEtalLibre = marche.trouverEtalLibre();
		marche.utiliserEtal(indiceEtalLibre, vendeur, produit, nbProduit);
		
		chaine.append("\nLe vendeur " + vendeur.getNom() + " vend des " + produit + " à l'étal n°" + (indiceEtalLibre+1) + ".\n");
		return chaine.toString();
	}
	
	public String rechercherVendeursProduit(String produit) {
		StringBuilder chaine = new StringBuilder();
		
		Etal[] etalProduit = marche.trouverEtals(produit);
		if (etalProduit.length == 0) {
			chaine.append("Il n'y a pas de vendeur qui propose des fleurs au marché.\n");
			
		} else if (etalProduit.length == 1) {
			chaine.append("Seul le vendeur " + etalProduit[0].getVendeur().getNom() + " propose des " + produit + " au marché.\n");
			
		} else {
			chaine.append("Les vendeurs qui proposent des " + produit + " sont :\n");
			for (int i=0; i < etalProduit.length; i++) {
				chaine.append("- " + etalProduit[i].getVendeur().getNom() + "\n");
			}
		}
		
		return chaine.toString();
	}
	
	public Etal rechercherEtal(Gaulois vendeur) {
		return marche.trouverVendeur(vendeur);
	}
	
	public String partirVendeur(Gaulois vendeur) {
		StringBuilder chaine = new StringBuilder();
		
		Etal etal = marche.trouverVendeur(vendeur);
		chaine.append(etal.libererEtal());
		
		return chaine.toString();
	}
	
	public String afficherMarche() {
		 return marche.afficherMarche();
	}
	
	
	
	//Classe interne
	private static class Marche {
		private Etal[] etals;
		
		private Marche(int nbEtals) {
			etals = new Etal[nbEtals];
			for (int i=0; i < nbEtals; i++) {
				etals[i] = new Etal();
			}
		}
		
		private void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
			etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
		}
		
		private int trouverEtalLibre() {
			for (int i=0; i < etals.length; i++) {
				if (!(etals[i].isEtalOccupe())) {
					return i;
				}
			}
			return -1;
		}
		
		private Etal[] trouverEtals(String produit) {
			Etal[] etalProduit;
			int nbEtalOccupe = 0;
			for (int i=0; i < etals.length; i++) {
				if (etals[i].isEtalOccupe() && etals[i].contientProduit(produit)) {
					nbEtalOccupe++;
				}
				
			}
			etalProduit = new Etal[nbEtalOccupe];
			
			int i = 0;
			for (int j=0; j < etals.length; j++) {
				if (etals[j].isEtalOccupe() && etals[j].contientProduit(produit)) {
					etalProduit[i] = etals[j];
					i++;
				}
			}
			return etalProduit;
		}
		
		private Etal trouverVendeur(Gaulois gaulois) {
			for (int i=0; i < etals.length; i++) {
				if (etals[i].getVendeur() == gaulois) {
					return etals[i];
				}
			}
			return null;
		}
		
		private String afficherMarche() {
			StringBuilder chaine = new StringBuilder();
			int nbEtalVide = 0;
			
			chaine.append("Le marché du village " + "..." + " possède plusieurs étals : \n" );
			
			for (int i=0; i < etals.length; i++) {
				if (etals[i].isEtalOccupe()) {
					String afficheEtal = etals[i].afficherEtal();
				
		            String[] parts = afficheEtal.split(" est garni de ");
		            String quantiteProduit = parts[1];
		            String[] quantiteEtProduit = quantiteProduit.split(" ");
		            String quantite = quantiteEtProduit[0];
		            String produit = quantiteEtProduit[1];
		            
					chaine.append(etals[i].getVendeur().getNom() + " vend " + quantite + " " +  produit);
				} else {
					nbEtalVide++;
				}
			}
			
			if (nbEtalVide > 0) {
				chaine.append("Il reste " + nbEtalVide + " étals non utilisés dans le marché.\n");
			}
			return chaine.toString();
		}
	}
	
	public class VillageSansChefException extends RuntimeException {
		
		public VillageSansChefException() {
		}
		
		public VillageSansChefException(String message) {
			super(message);
		}
		
		public VillageSansChefException(Throwable cause) {
			super(cause);
		}
		
		public VillageSansChefException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
















