package org.eclipse.transformer.Utility;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;

public class SpecificationReader {

	  static public String GetSpecification( InputStream is){
	   
		  String specification = null;
		  
		  
			try {
				StringWriter writer = new StringWriter();
				IOUtils.copy(is, writer, "UTF-8");
				specification = writer.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  
	   return specification;
	  }
	  
	  
}
