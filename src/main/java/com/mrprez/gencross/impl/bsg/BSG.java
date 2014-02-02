package com.mrprez.gencross.impl.bsg;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.PropertiesList;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.history.HistoryUtil;
import com.mrprez.gencross.history.ProportionalHistoryFactory;
import com.mrprez.gencross.value.Value;

public class BSG extends Personnage {
	
	
	
	
	@Override
	public void calculate() {
		super.calculate();
		if(getProperty("Complications").getSubProperties().isEmpty()){
			errors.add("Vous devez avoir au moins une \"Complication\"");
		}
		if(phase.equals("création")){
			checkComplication();
		}
	}

	public void changeSkillValue(Property skill, Value oldValue){
		skill.getSubProperties().setFixe(!skill.getValue().equals(skill.getMax()));
	}
	
	public void speciality(Property speciality){
		Property skill = (Property) speciality.getOwner();
		skill.setEditable(skill.getSubProperties().isEmpty());
	}
	
	public void checkComplication(){
		Integer historySum = HistoryUtil.sumHistoryOfSubTree(history, getProperty("Complications")).get("Trait");
		if(historySum!=null && historySum<-30){
			errors.add("Vous avez "+(-historySum)+" pts dans les Complications (max=30)");
		}
	}
	
	public void passToABord(){
		getProperty("Attribute").setHistoryFactory(new ProportionalHistoryFactory("AP", 8));
		for(Property skill : getProperty("Skills and Specialties").getSubProperties()){
			skill.setHistoryFactory(new ProportionalHistoryFactory("AP", 3));
			PropertiesList specialities = skill.getSubProperties();
			specialities.getDefaultProperty().setHistoryFactory(new ProportionalHistoryFactory("AP", 3));
			for(Property speciality : specialities){
				speciality.setHistoryFactory(new ProportionalHistoryFactory("AP", 3));
			}
		}
		getProperty("Assets").setHistoryFactory(new ProportionalHistoryFactory("AP", 7));
		getProperty("Complications").setHistoryFactory(new ProportionalHistoryFactory("AP", -7));
		
	}

	
	
	

}
