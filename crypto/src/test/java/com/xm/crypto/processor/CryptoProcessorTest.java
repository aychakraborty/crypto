package com.xm.crypto.processor;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CryptoProcessorTest {
	
	@InjectMocks
	CryptoProcessor processor;
	
	@org.junit.Test
	@Test
	public void getCryptoMineMapByInputTest() {
		Assert.assertNotNull(processor.getCryptoMineMapByInput("BTC"));
	}
	
	@org.junit.Test
	@Test
	public void getSortedCryptoListTest() {
		Assert.assertNotNull(processor.getSortedCryptoList());
	}

	@org.junit.Test
	@Test
	public void getCryptoMineMapByDateTest() {
		Assert.assertNotNull(processor.getHighestNormalisedCryptoByDate("01-01-2022"));
	}
}
