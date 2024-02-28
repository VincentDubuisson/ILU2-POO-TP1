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
		
		chaine.append("\nLe vendeur " + vendeur.getNom() + " vend des " + produit + " à l'étal n°" + (indiceEtalLibre+1) + ".");
		return chaine.toString();
	}
	
	public String rechercherVendeursProduit(String produit) {
		StringBuilder chaine = new StringBuilder();
		
		Etal[] etalProduit = marche.trouverEtals(produit);
		if (etalProduit.length == 0) {
			chaine.append("Il n'y a pas de vendeur qui propose des fleurs au marché.");
		} else if (etalProduit.length == 1) {
			
		}
		for (int i=0; i < etalProduit.length; i++) {
			etalProduit[i].getVendeur().getNom();
		}
		
		return chaine.toString();
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
				if (etals[i].contientProduit(produit)) {
					nbEtalOccupe++;
				}
			}
			etalProduit = new Etal[nbEtalOccupe];
			
			int i = 0;
			for (int j=0; j < etals.length; j++) {
				if (etals[j].contientProduit(produit)) {
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
			for (int i=0; i < etals.length; i++) {
				if (etals[i].isEtalOccupe()) {
					chaine.append(etals[i].afficherEtal() + "\n");
				} else {
					nbEtalVide++;
				}
			}
			
			if (nbEtalVide > 0) {
				chaine.append("Il reste " + nbEtalVide + " étals non utilisés dans le marché");
			}
			return chaine.toString();
		}
	}
}
















