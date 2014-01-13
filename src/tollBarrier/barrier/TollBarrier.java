/* 
 * ENSICAEN
 * 6 Boulevard Marechal Juin 
 * F-14050 Caen Cedex 
 *
 * This file is owned by ENSICAEN students.
 * No portion of this document may be reproduced, copied
 * or revised without written permission of the authors.
 */
/**
 * @author Gaëtan Le Barbé gaetan.lebarbe@ecole.ensicaen.fr
 * @version 0.0.1
 * @date 3 déc. 2013
 */
package tollBarrier.barrier;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

import tollBarrier.bornes.BoManuelle;
import tollBarrier.bornes.Borne;
import tollBarrier.bornes.FabriqueDeBorne;
import tollBarrier.bornes.exceptions.NotAValidBorneTypeException;
import tollBarrier.vehicule.MoyenDePaiment;

import java.util.ArrayList;

import tollBarrier.vehicule.exceptions.PasDeVehiculeTrouveException;
import tollBarrier.vehicule.vehiculesObjects.Camion;
import tollBarrier.vehicule.vehiculesObjects.Vehicule;

public class TollBarrier
{
	private static TollBarrier instance;

	private LinkedList<Borne> bornes;
	private LinkedList<Vehicule> vehicules;
	private ArrayList<Debit> debits;
	private static boolean running = false;
	
	public static boolean isRunning()
	{
		return running;
	}

	private TollBarrier()
	{
		bornes = new LinkedList<Borne>();
		vehicules = new LinkedList<Vehicule>();
		debits = new ArrayList<Debit>();
	}

	public static void reset()
	{
		instance = new TollBarrier();
		running = false;
	}

	/**
	 * @return
	 */
	public void addDebit(String typeVehicule, Integer nbParMinute,
			Collection<String> typePaiement)
	{
		for (String s : typePaiement)
			debits.add(new Debit(typeVehicule, nbParMinute, s, vehicules, this));
	}

	public void addDebit(String typeVehicule, Integer nbParMinute,
			HashSet<MoyenDePaiment> mdp)
	{
		for (MoyenDePaiment m : mdp)
			debits.add(new Debit(typeVehicule, nbParMinute, m.name(),
					vehicules, this));
	}

	public static TollBarrier getInstance()
	{
		if (instance == null)
			instance = new TollBarrier();
		return instance;
	}

	public void addBorne(String typeborne)
	{
		try
		{
			bornes.add(new FabriqueDeBorne().creerBorne(typeborne));
		} catch (NotAValidBorneTypeException e)
		{
			e.printStackTrace();
		}
	}

	public static boolean compatible(Borne borne, Vehicule vehicule)
	{
		if (vehicule instanceof Camion && !(borne instanceof BoManuelle))
			return false;
		for (MoyenDePaiment mdp : vehicule.getMoyensDePaiment())
			if (borne.getMoyensDePaiment().contains(mdp))
			{
				return true;
			}
		return false;
	}

	public Vehicule getVehicule(Borne borne)
			throws PasDeVehiculeTrouveException
	{
		synchronized (vehicules)
		{
			if (vehicules == null || vehicules.isEmpty())
				throw new PasDeVehiculeTrouveException();
			LinkedList<Vehicule> vehiculesCopy = new LinkedList<Vehicule>(
					vehicules);
			for (Vehicule v : vehiculesCopy)
			{
				if (compatible(borne, v))
				{
					return vehicules.remove(vehiculesCopy.indexOf(v));
				}
			}
			throw new PasDeVehiculeTrouveException();
		}
	}

	/**
	 * @return
	 */
	/*
	 * public Float getTempsPassageMoyen() { int nbBornes = getNombreBornes();
	 * float sum = 0;
	 * 
	 * for (int i = 0; i < nbBornes; i++) { sum +=
	 * getTempsPassageMoyenParBorne(i); } return sum / nbBornes; TODO }
	 */

	/**
	 * @return
	 */
	/*
	 * public Float getTempsPassageMoyenParBorne(int numBorne) { Borne borne =
	 * bornes.get(numBorne); return borne.getTempsPassage(); TODO }
	 */

	/**
	 * @return
	 */
	public Integer getNombreVehiculeEnAttente()
	{
		return vehicules.size();
	}

	/**
	 * @return
	 */
	public void addIntervenant()
	{
		// TODO Auto-generated method stub

	}

	public void demarrerSimulation()
	{
		running = true;
		for (Borne b : bornes)
			b.start();
		for (Debit d : debits)
			d.start();
	}

	public void arreterSimulation()
	{
		running = false;
	}

	/**
	 * @return
	 */
	public Integer getNombreBornes(String typeBorne)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	public Integer getNombreBornes()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	public Float getDebitEntree()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	public Float getTempsPassageMoyenParTypeDeBorne(String borne)
			throws NotAValidBorneTypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	public Float getTempsInnocupationBorne(String typeborne)
			throws NotAValidBorneTypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	public Integer getNombreIntervenants()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 */
	public void removeIntervenant()
	{
		// TODO Auto-generated method stub

	}

	public Integer getNextNumeroBorne()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args)
	{
		TollBarrier barriere = getInstance();

		Scanner sc = new Scanner(System.in);

		System.out.println("Combien de débits ?");
		int nbDebits = Integer.parseInt(sc.nextLine());
		for (int i = 0; i < nbDebits; i++)
		{
			System.out.println("Débit " + i);
			System.out
					.println("Quel type de véhicules ((V)oiture,(C)amion,(D)eux-roues) ?");
			String typeV = sc.nextLine();
			System.out.println("Combien de véhicules par minute ?");
			int nbV = Integer.parseInt(sc.nextLine());
			System.out
					.println("Quel Moyen de paiment ((C)B,(A)bonnement,(L)iquide,(T)elepeage) ?");
			String typeP = sc.nextLine();
			HashSet<String> typesP = new HashSet<String>();
			typesP.add(typeP);
			barriere.addDebit(typeV, nbV, typesP);
		}

		System.out.println("Combien de bornes ?");
		int nbBornes = Integer.parseInt(sc.nextLine());
		for (int i = 0; i < nbBornes; i++)
		{
			System.out.println("Borne " + i);
			System.out
					.println("Quel type de borne (T)elepeage, (A)utomatique, (M)anuelle ?");
			String typeB = sc.nextLine();

			barriere.addBorne(typeB);
		}

		sc.close();

		for (Borne b : barriere.bornes)
			b.start();

		for (Debit d : barriere.debits)
			d.start();
	}

	public void add(Vehicule v)
	{
		synchronized (vehicules)
		{
			vehicules.add(v);
			System.out.println(v + " arrive au péage");
		}
	}
}
