
package tollBarrier.barrier;

public interface TollBarrierListener
{
	void updateVehiculesEnAttente();
	void updateTempsPassageMoyen();
	void updateArgentEncaisse();
	
	void updateAll();

}
