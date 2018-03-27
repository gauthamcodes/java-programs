package com.gautham.twitterstream;

import java.io.IOException;

import twitter4j.JSONException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws JSONException
    {
        try {
			new TwitterConsumer().consume();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
