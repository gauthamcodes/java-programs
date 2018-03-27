package com.gautham;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.sql.SQLException;

import com.gautham.sourceconnector.utils.BlobUtils;
import com.microsoft.azure.storage.StorageException;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main(String[] args)
			throws InvalidKeyException, URISyntaxException, StorageException, FileNotFoundException, IOException, SQLException
    {
        //BlobUtils.upload("dummydata4.csv","C:\\Users\\Gautham.Manivannan\\Project\\AscendLearning\\tmp\\");
		BlobUtils.move(null);
        //BlobUtils.delete();
		
		//Connection con = setupConnectionPool().getConnection();
    }
}
