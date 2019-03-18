package io.github.lr.querythreads.services;

import java.text.MessageFormat;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;

import io.github.lr.querythreads.entities.AbstractEntity;
import io.github.lr.querythreads.utils.UtilsHelper;

/**
 * Servicio de búsqueda paginada en Base de Datos. Cada página se obtiene en paralelo.
 * 
 * @author lravanal
 *
 * @param <E> Entidad
 */
public abstract class DBSearchService<E extends AbstractEntity> {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String ID = "id";
	
	@Value("${paged.search}") private boolean active;

	@Value("${concurrent.searches}") protected int quantity;
	@Value("${page.size}") protected int size;
	
	@Autowired protected JpaRepository<E, Long> repository;
	
	@SuppressWarnings("unchecked")
	public List<E> findAllPaging(Object ... params) {
		logger.debug("Buscando registros");
		
		if (!active) {
			logger.debug("Búsqueda paginada desactivada");

			long start = System.currentTimeMillis();
			
			List<E> records = null;
			
			try {
				records = findAll(params);
			
			} catch (RuntimeException re) {
				logger.error("No se pudo completar la búsqueda de los registros", re);
				
				throw re;
			}
			
			long executionTime = System.currentTimeMillis() - start;
			
			logger.debug(MessageFormat.format("Registros encontrados en{0}", UtilsHelper.convertDuration(executionTime)));
			
			logger.info(MessageFormat.format("Cantidad de registros leídos: {0}", records.size()));
			
			return records;
		}
		
		final Queue<E> records = new ConcurrentLinkedQueue<E>();
		
		long start = System.currentTimeMillis();

		final int count = (int) (count(params) + size - 1) / size;
		final CompletableFuture<Void>[] futures = new CompletableFuture[count];
		
		logger.debug(MessageFormat.format("Cantidad de páginas: {0}", count));
		
		final ExecutorService executor = Executors.newFixedThreadPool(quantity);
		
		for (int i = 0; i < count; i++) {
			final int page = i;
			
			futures[i] = CompletableFuture.runAsync(() -> {
				records.addAll(findAll(PageRequest.of(page, size, Direction.ASC, ID), params).getContent());
			}, executor);
		}
		
		try {
			CompletableFuture.allOf(futures).join();
		
		} catch (CompletionException ce) {
			logger.error("No se pudo completar la búsqueda paginada de los registros", ce);
			
			throw ce;
		}
		
		executor.shutdown();
		
		long executionTime = System.currentTimeMillis() - start;
	
		logger.debug(MessageFormat.format("Registros encontrados en{0}", UtilsHelper.convertDuration(executionTime)));
		
		logger.info(MessageFormat.format("Cantidad de registros leídos: {0}", records.size()));

		return records.stream().sorted((record1, record2) -> Long.compare(record1.getId(), record2.getId())).collect(Collectors.toList());
	}
	
	protected long count(Object ... params) {
		return repository.count();
	}
	
	protected List<E> findAll(Object ... params) {
		return repository.findAll();
	}

	protected Page<E> findAll(PageRequest pageRequest, Object ... params) {
		return repository.findAll(pageRequest);
	}
	
}
