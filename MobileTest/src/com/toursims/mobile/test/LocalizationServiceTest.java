package com.toursims.mobile.test;

import android.content.Intent;
import android.test.ServiceTestCase;

import com.toursims.mobile.LocalizationService;

/**
 * JUnit tests for the LocalizationService class 
 */
public class LocalizationServiceTest extends ServiceTestCase<LocalizationService> {

	/**
	 * Default constructor, required by JUnit
	 */
	public LocalizationServiceTest() {
		super(LocalizationService.class);
	}
	
	/**
	 * Standard JUnit method, called before a test is launched
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	/**
	 * Test the onCreate method (basic startup/shutdown of the service)
	 */
	public void testStartable() {
		startService(new Intent().setClass(getContext(), LocalizationService.class));
	}
	
}
