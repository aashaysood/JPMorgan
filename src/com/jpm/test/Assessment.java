/**
 * 
 */
package com.jpm.test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Aashay
 *
 */
public class Assessment {

	static List<Comparable> instructionList = null;
	static String entity;
	static String BS;
	static double agreedFx=0.0;
	static String currency;
	static long units;
	static double unitPrice = 0.0;
	static int count = 0;
	static List<Entity> incomingList = new ArrayList<Entity>();
	static List<Entity> outgoingList = new ArrayList<Entity>();
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		DayOfWeek dow = null;
		Date setlDate = null;
		Calendar cal = Calendar.getInstance();
		double totalInAmount = 0.0;
		double totalOutAmount = 0.0;
		
		for(int i=0;i<3;i++){
			setInstruction();
			getInstruction();
			
			//check if Settlement Date needs to be moved
			if(currency.equals("AED")||currency.equals("SAR")){
				setlDate = getSetlDate((String)instructionList.get(5));
				dow = dateUtility((String)instructionList.get(5));
				if(dow==DayOfWeek.FRIDAY){
					cal.setTime(setlDate);
					cal.add(Calendar.DATE, 2);
					setlDate = cal.getTime();
				}else if(dow==DayOfWeek.SATURDAY){
					cal.setTime(setlDate);
					cal.add(Calendar.DATE, 1);
					setlDate = cal.getTime();
				}
			}else{
				setlDate = getSetlDate((String)instructionList.get(5));
				dow = dateUtility((String)instructionList.get(5));
				if(dow==DayOfWeek.SATURDAY){
					cal.setTime(setlDate);
					cal.add(Calendar.DATE, 2);
					setlDate = cal.getTime();
				}else if(dow==DayOfWeek.SUNDAY){
					cal.setTime(setlDate);
					cal.add(Calendar.DATE, 1);
					setlDate = cal.getTime();
				}
			}
			
			//Set trade amount
			double tradeAmount=0.0;
			if(BS.equals("B")){
				tradeAmount = getTradeAmt(agreedFx, units, unitPrice);
				totalOutAmount=totalOutAmount+tradeAmount;
				outgoingList.add(new Entity((String)instructionList.get(0), tradeAmount));
			}else if(BS.equals("S")){
				tradeAmount = getTradeAmt(agreedFx, units, unitPrice);
				totalInAmount=totalInAmount+tradeAmount;
				incomingList.add(new Entity((String)instructionList.get(0), tradeAmount));
			}
		}
		
		System.out.println("The total Incoming Amount is: " + totalInAmount);
		System.out.println("The total Outgoing Amount is: " + totalOutAmount);

		// Incoming List Sort
		System.out.println("Incoming Sorted List");
		System.out.println("Entity Name		Entity Amount");
		incomingList.sort(Comparator.comparing(Entity::getAmount));
		for(int i=incomingList.size()-1;i>=0;i--){
			System.out.println(((Entity)incomingList.get(i)).getName()+"			"+((Entity)incomingList.get(i)).getAmount());
		}
		
		// Outgoing List Sort
		System.out.println("Outgoing Sorted List");
		System.out.println("Entity Name		Entity Amount");
		outgoingList.sort(Comparator.comparing(Entity::getAmount));
		for(int i=outgoingList.size()-1;i>=0;i--){
			System.out.println(((Entity)outgoingList.get(i)).getName()+"			"+((Entity)outgoingList.get(i)).getAmount());
		}
		
	}
	
	public static Date getSetlDate(String setlDate){
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);
		LocalDate date = LocalDate.parse(setlDate, formatter);
		return java.sql.Date.valueOf(date);
	}
	
	public static double getTradeAmt(double agreedFx, long units, double unitPrice){
		
		double tradeAmt=0.0;
		tradeAmt = agreedFx*units*unitPrice;
		return tradeAmt;
	}
	
	public static DayOfWeek dateUtility(String setlDate){
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);
		LocalDate date = LocalDate.parse(setlDate, formatter);
		DayOfWeek dow = date.getDayOfWeek();
		return dow;
	}
	
	public static void getInstruction(){
		
		entity = (String)instructionList.get(0);
		BS = (String)instructionList.get(1);
		agreedFx = (double)instructionList.get(2);
		currency = (String)instructionList.get(3);
		units = (int)instructionList.get(6);
		unitPrice = (double)instructionList.get(7);
		
	}
	public static void setInstruction(){
		
		instructionList = new ArrayList<Comparable>();
		if(count==0){
			instructionList.add("foo");
			instructionList.add("B");
			instructionList.add(0.50);
			instructionList.add("SGP");
			instructionList.add("01 Jan 2016");
			instructionList.add("02 Jan 2016");
			instructionList.add(200);
			instructionList.add(100.25);
		}else if(count==1){
			instructionList.add("bar");
			instructionList.add("S");
			instructionList.add(0.22);
			instructionList.add("AED");
			instructionList.add("05 Jan 2016");
			instructionList.add("08 Jan 2016");
			instructionList.add(450);
			instructionList.add(150.5);
		}else if(count==2){
			instructionList.add("bar");
			instructionList.add("S");
			instructionList.add(0.25);
			instructionList.add("AED");
			instructionList.add("05 Jan 2016");
			instructionList.add("07 Jan 2016");
			instructionList.add(450);
			instructionList.add(150.5);
		}
		count++;
	}
}
