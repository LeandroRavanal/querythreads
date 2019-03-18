package io.github.lr.querythreads.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.lr.querythreads.entities.Baby;
import io.github.lr.querythreads.managers.BabyManager;

/**
 * Controlador que agrupa las operaciones (puntos de acceso) a la aplicacion.
 * 
 * @author lravanal
 *
 */
@RestController	
@RequestMapping("/api/v1")
public class FrontController {
	
	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private BabyManager babyManager;

	@GetMapping(value = "/babies")
	public ResponseEntity<List<Baby>> babies() {
		logger.debug("Obteniendo el listado de bebés");
		
		return new ResponseEntity<List<Baby>>(babyManager.getBabies(), HttpStatus.OK);
	}
	
}
