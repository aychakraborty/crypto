package com.xm.crypto.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.xm.crypto.model.CryptoDetails;

/**
 * This class deals with reading CSV files from resource/files folder
 * 
 * @author Ayan_Chakraborty
 *
 */
public class FileReader {

	/**
	 * This method is used to store data in CryptoDetails model
	 * 
	 * @param csvLine
	 * @return CryptoDetails
	 */
	private CryptoDetails getCryptoDetail(String csvLine) {
		String[] values = csvLine.split(",");
		CryptoDetails cryptoDetails = new CryptoDetails();
		cryptoDetails.setTimeStamp(new Date(Long.valueOf(values[0])));
		cryptoDetails.setSymbol(values[1]);
		cryptoDetails.setPrice(Double.valueOf(values[2]));

		return cryptoDetails;
	}

	/**
	 * This method accepts the particular folder and lists all the files present in
	 * the folder which is ready to be read
	 * 
	 * @param folder
	 * @return List<File>
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public List<File> getAllFilesFromResource(String folder) throws URISyntaxException, IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(folder);
		List<File> collect = Files.walk(Paths.get(resource.toURI())).filter(Files::isRegularFile).map(x -> x.toFile())
				.collect(Collectors.toList());
		return collect;
	}

	/**
	 * This method accepts file path, reads all the lines of the file and creates a
	 * list of CryptoDetails model
	 * 
	 * @param filePath
	 * @return List<CryptoDetails>
	 */
	public List<CryptoDetails> getCryptoDetails(String filePath) {
		List<CryptoDetails> cryptoDetailsList = new ArrayList<>();
		ClassLoader classLoader;
		try {
			classLoader = getClass().getClassLoader();
			InputStream inputStream = classLoader.getResourceAsStream(filePath);
			try (InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
					BufferedReader reader = new BufferedReader(streamReader)) {

				cryptoDetailsList = reader.lines().skip(1).map(l -> {
					return getCryptoDetail(l);
				}).collect(Collectors.toList());

			} catch (FileNotFoundException e) {
				throw new RuntimeException("File Not Found!!");
			}
		} catch (IOException e1) {
			throw new RuntimeException("Class Loading Failed!!");
		}

		return cryptoDetailsList;
	}

}
