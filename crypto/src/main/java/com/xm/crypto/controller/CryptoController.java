package com.xm.crypto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.xm.crypto.processor.CryptoProcessor;

/**
 * This file contains all the required endpoints of the APIs
 * 
 * @author Ayan_Chakraborty
 *
 */
@RestController
public class CryptoController {

	@Autowired
	CryptoProcessor processor;

	/**
	 * This API returns a descending sorted list of all the cryptos, comparing the
	 * normalized range (i.e. (max-min)/min)
	 * 
	 * @return ResponseEntity<Object>
	 */
	@GetMapping(value = "/api/sortcryptos")
	public ResponseEntity<Object> getSortedCyptos() {
		return new ResponseEntity<Object>(processor.getSortedCryptoList(), HttpStatus.OK);
	}

	/**
	 * This API returns the oldest/newest/min/max values for a requested crypto
	 * 
	 * @param crypto
	 * @return ResponseEntity<Object>
	 */
	@GetMapping(value = "/api/{crypto}")
	public ResponseEntity<Object> getCryptoDetails(@PathVariable("crypto") String crypto) {
		return new ResponseEntity<Object>(processor.getCryptoMineMapByInput(crypto), HttpStatus.OK);
	}

	/**
	 * This API returns the crypto with the highest normalized range for a specific
	 * day (dd-mm-yyyy)
	 * 
	 * @param date
	 * @return ResponseEntity<Object>
	 */
	@GetMapping(value = "/api/date/{date}")
	public ResponseEntity<Object> getHighestNormalisedCryptoByDate(@PathVariable("date") String date) {
		return new ResponseEntity<Object>(processor.getHighestNormalisedCryptoByDate(date), HttpStatus.OK);
	}

}
