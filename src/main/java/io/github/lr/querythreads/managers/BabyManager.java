package io.github.lr.querythreads.managers;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.github.lr.querythreads.entities.Baby;
import io.github.lr.querythreads.services.DBSearchService;

/**
 * Administrador para la obtención de la lista de entidades.
 * 
 * @author lravanal
 *
 */
@Component
public class BabyManager {
	
	public final Logger logger = LoggerFactory.getLogger(this.getClass());	

	@Autowired private DBSearchService<Baby> babyDBSearch;
	
	private CountDownLatch conditionMetLatch = new CountDownLatch(1);

	private List<Baby> babiesList;
	
	@EventListener
	public void onApplicationEvent(ApplicationReadyEvent event) {
		logger.debug("Evento de inicialización de aplicación");
		
		try {
			babiesList = babyDBSearch.findAllPaging();

		} catch (RuntimeException re) {
			logger.info("Ocurrió un error al obtener la información ...");

		}
			
		conditionMetLatch.countDown();
	}
	
	public List<Baby> getBabies() {
		boolean result = waitForConditionToBeMet();
		
		if (result && !babiesList.isEmpty()) {
			return babiesList; 
		}
		
		throw new RuntimeException("No se pudo obtener la información");
	}
	
    private boolean waitForConditionToBeMet() {
        try {
            return conditionMetLatch.await(30, TimeUnit.SECONDS);
        
        } catch (InterruptedException ie) {
            logger.error("Espera interrumpida", ie);
        
        }
        
        return false;
    }
	
}
