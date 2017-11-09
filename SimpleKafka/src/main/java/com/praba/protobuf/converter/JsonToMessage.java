package com.praba.protobuf.converter;

import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;
import com.praba.exceptions.InvalidMessageFormatException;
import com.praba.protobuf.EmployeeProto;
import com.praba.protobuf.EmployeeProto.Employee;

/**
 * This utility class converts Json String to ProtoBuf <code>Message</code>
 * format
 * 
 * @author Praba
 *
 */
public class JsonToMessage {

	/**
	 * TODO: need to enhance to ProtoBuf implementation
	 * 
	 * @param jsonString
	 * @return
	 * @throws InvalidJsonFormatException
	 */
	public static Message parseJson(String jsonString) throws InvalidMessageFormatException {
		Employee.Builder builder = EmployeeProto.Employee.newBuilder();

		
		
		
		

		try {
			JsonFormat.merge(jsonString, builder);
		} catch (com.googlecode.protobuf.format.JsonFormat.ParseException e) {
			throw new InvalidMessageFormatException("Invalid Message Format received");
		
		}

		return builder.build();
	}
}
