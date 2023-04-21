package com.xm.crypto.processor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.xm.crypto.model.CryptoDetails;
import com.xm.crypto.utility.FileReader;

/**
 * This is a service/processor class that processes individual requests from the
 * API using business logics
 * 
 * @author Ayan_Chakraborty
 *
 */
@Service
public class CryptoProcessor {

	public static final String OLDEST = "OLDEST";
	public static final String NEWEST = "NEWEST";
	public static final String MAX = "MAX";
	public static final String MIN = "MIN";

	// This static map is used to map cryptos with it's normalised value
	public static Map<String, Double> cryptoNormalisedMap = new HashMap<>();

	/**
	 * This method is used to map individual cryptos with it's corresponding model
	 * list/data in the CSV file
	 * 
	 * @return Map<String, List<CryptoDetails>>
	 */
	private Map<String, List<CryptoDetails>> getCryptoDetailsMap() {
		FileReader reader = new FileReader();
		String resourcePath = "files";
		List<String> filesNames;
		Map<String, List<CryptoDetails>> cryptoDetailsMap = new HashMap<>();
		try {
			filesNames = reader.getAllFilesFromResource(resourcePath).stream().map(file -> file.getName())
					.collect(Collectors.toList());
			filesNames.forEach(fileName -> {
				String[] cryptoName = fileName.split("_");
				List<CryptoDetails> cryptoDetailsList = reader.getCryptoDetails(resourcePath + "/" + fileName);
				cryptoDetailsMap.put(cryptoName[0], cryptoDetailsList);
			});
		} catch (URISyntaxException | IOException e) {
			throw new RuntimeException("File Reading Issue!!");
		}
		return cryptoDetailsMap;
	}

	/**
	 * This method is used to map individual crypto with a map that contains crypto
	 * related business logic and it's value
	 * 
	 * @return Map<String, Map<String, String>>
	 */
	private Map<String, Map<String, String>> getCryptoMineMap() {
		Map<String, Map<String, String>> cryptoMineMap = new HashMap<>();
		Map<String, List<CryptoDetails>> cryptoDetailsMap = getCryptoDetailsMap();
		cryptoDetailsMap.keySet().forEach(crypto -> {
			Map<String, String> mineMap = new HashMap<>();
			List<CryptoDetails> cryptoDetailsList = cryptoDetailsMap.get(crypto);
			Double maxPrice = cryptoDetailsList.stream().map(CryptoDetails::getPrice).max(Double::compareTo).get();
			Double minPrice = cryptoDetailsList.stream().map(CryptoDetails::getPrice).min(Double::compareTo).get();
			Double normalisedPrice = (maxPrice - minPrice) / minPrice;
			mineMap.put(MAX, maxPrice.toString());
			mineMap.put(MIN, minPrice.toString());
			mineMap.put(NEWEST,
					cryptoDetailsList.stream().map(CryptoDetails::getTimeStamp).max(Date::compareTo).get().toString());
			mineMap.put(OLDEST,
					cryptoDetailsList.stream().map(CryptoDetails::getTimeStamp).min(Date::compareTo).get().toString());
			cryptoNormalisedMap.put(crypto, normalisedPrice);
			cryptoMineMap.put(crypto, mineMap);
		});
		return cryptoMineMap;
	}

	/**
	 * This method accepts a particular crypto and returns it's specific business
	 * data
	 * 
	 * @param crypto
	 * @return Map<String, String>
	 */
	public Map<String, String> getCryptoMineMapByInput(String crypto) {
		Map<String, Map<String, String>> cryptoMineMap = getCryptoMineMap();
		return cryptoMineMap.get(crypto);
	}

	/**
	 * This method returns sorted list of cryptos in descending order based on their
	 * normalised value
	 * 
	 * @return List<String>
	 */
	public List<String> getSortedCryptoList() {
		getCryptoMineMap();
		Map<String, Double> cryptoSortedNormalisedMap = cryptoNormalisedMap.entrySet().stream()
				.sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
						(oldValue, newValue) -> oldValue, LinkedHashMap::new));
		List<String> sortedCryptoList = new ArrayList<String>(cryptoSortedNormalisedMap.keySet());
		Collections.reverse(sortedCryptoList);
		return sortedCryptoList;
	}

	/**
	 * This method accepts a particular date and finds all the crypto details of
	 * that particular date and the maps individual crypto with it's normalised
	 * value
	 * 
	 * @param specifieddate
	 * @return Map<String, Double>
	 */
	private Map<String, Double> getCryptoMineMapByDate(String specifieddate) {
		Map<String, Double> cryptoMineMapByDate = new HashMap<>();
		Map<String, List<CryptoDetails>> cryptoDetailsMap = getCryptoDetailsMap();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
		cryptoDetailsMap.keySet().forEach(crypto -> {
			List<CryptoDetails> cryptoDetailsList = cryptoDetailsMap.get(crypto);
			List<CryptoDetails> cryptoDetailsOnDateList = new ArrayList<>();
			cryptoDetailsList.forEach(cryptoDetails -> {
				if (formatter.format(cryptoDetails.getTimeStamp()).equals(specifieddate)) {
					cryptoDetailsOnDateList.add(cryptoDetails);
				}
			});
			Double maxPrice = cryptoDetailsOnDateList.stream().map(CryptoDetails::getPrice).max(Double::compareTo)
					.get();
			Double minPrice = cryptoDetailsOnDateList.stream().map(CryptoDetails::getPrice).min(Double::compareTo)
					.get();
			Double normalisedPrice = (maxPrice - minPrice) / minPrice;
			cryptoMineMapByDate.put(crypto, normalisedPrice);
		});
		return cryptoMineMapByDate;
	}

	/**
	 * This method accepts a particular date and returns the highest normalised
	 * crypto for that particular date
	 * 
	 * @param specifieddate
	 * @return String
	 */
	public String getHighestNormalisedCryptoByDate(String specifieddate) {
		Map<String, Double> cryptoMineMapByDate = getCryptoMineMapByDate(specifieddate);
		Entry<String, Double> maxEntry = Collections.max(cryptoMineMapByDate.entrySet(),
				(Entry<String, Double> e1, Entry<String, Double> e2) -> e1.getValue().compareTo(e2.getValue()));
		return maxEntry.getKey();
	}

}
