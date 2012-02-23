package org.saga.statistics;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.saga.config.AbilityConfiguration;
import org.saga.config.BalanceConfiguration;
import org.saga.config.ProficiencyConfiguration;
import org.saga.config.SkillConfiguration;
import org.saga.economy.EconomyMessages;
import org.saga.player.PlayerMessages.ColorCircle;
import org.saga.player.Proficiency.ProficiencyType;
import org.saga.player.SagaPlayer;
import org.saga.utility.MathUtil;
import org.saga.utility.StringBook;
import org.saga.utility.StringTable;
import org.saga.utility.TextUtil;




public class StatisticsMessages {
	

	// Colors:
	public static ChatColor veryPositive = ChatColor.DARK_GREEN; // DO NOT OVERUSE.
	
	public static ChatColor positive = ChatColor.GREEN;
	
	public static ChatColor negative = ChatColor.RED;
	
	public static ChatColor veryNegative = ChatColor.DARK_RED; // DO NOT OVERUSE.
	
	public static ChatColor unavailable = ChatColor.DARK_GRAY;
	
	public static ChatColor anouncment = ChatColor.AQUA;
	
	public static ChatColor normal1 = ChatColor.GOLD;
	
	public static ChatColor normal2 = ChatColor.YELLOW;
	
	public static ChatColor frame = ChatColor.DARK_GREEN;
	
	
	
	
	public static String classKills() {
	
		
		ColorCircle color = new ColorCircle().addColor(normal1).addColor(normal2);
		ArrayList<String> clazzes = ProficiencyConfiguration.config().getProficiencyNames(ProficiencyType.CLASS);
		StringTable table = new StringTable(color);

		clazzes.add("none");
		
		// Add headings:
		table.addLine(new String[]{"CLASS","VERSUS", "KILLS", "DEATHS"});
		
		
		// Add data:
		for (int i = 0; i < clazzes.size(); i++) {
			
			String thisClazz = clazzes.get(i);
			
			for (int j = 0; j < clazzes.size(); j++) {
				
				String otherClazz = clazzes.get(j);
				
				String[] row = new String[]{"",otherClazz, StatisticsManager.manager().getClazzKills(thisClazz, otherClazz).toString(), StatisticsManager.manager().getClazzKills(otherClazz, thisClazz).toString()};
				
				if(j == 0){
					row[0] = thisClazz;
				}
				
				table.addLine(row);
				
			}
			
		}
		
		table.collapse();
		
		StringBook book = new StringBook("class kills", color, 18);
		
		book.addTable(table);
		
		return book.framed(0, table.calcTotalWidth());
		
		
	}
	
	public static String skills() {
	
		
		ColorCircle color = new ColorCircle().addColor(normal1).addColor(normal2);
		ArrayList<String> skills = SkillConfiguration.config().getSkillNames();
		StringTable table = new StringTable(color);

		// Add headings:
		table.addLine(new String[]{"SKILL","UPGRADES", "COIN COST"});
		
		// Add data:
		StatisticsManager manager = StatisticsManager.manager();
		for (int i = 0; i < skills.size(); i++) {
			
			String name = skills.get(i);
			
			String[] row = new String[]{name, manager.getSkillUpgrades(name).toString(), EconomyMessages.coins(manager.getSkillUpgradeCoins(name))};
			
			table.addLine(row);
			
		}
		
		table.collapse();
		
		StringBook book = new StringBook("class kills", color, 18);
		
		book.addTable(table);
		
		return book.framed(0, table.calcTotalWidth());
		
		
	}

	public static String general() {
	
		
		ColorCircle color = new ColorCircle().addColor(normal1).addColor(normal2);
		StringTable table = new StringTable(color);

		// Add headings:
		table.addLine(new String[]{"STATISTIC","VALUE"});
		
		// Guardian stone:
		table.addLine(new String[]{"guardian stone restores", StatisticsManager.manager().getGuardStoneBreaks().toString()});
		table.addLine(new String[]{"guardian stone recharges", StatisticsManager.manager().getGuardStoneFixes().toString()});
		
		// Blocks:
		table.addLine(new String[]{"block data change", StatisticsManager.manager().getBlockDataChanges().toString()});
		
		table.collapse();
		
		StringBook book = new StringBook("general statistics", color, 10);
		
		book.addTable(table);
		
		return book.framed(0, table.calcTotalWidth());
		
		
	}

	public static String abilities() {
	
		
		ColorCircle color = new ColorCircle().addColor(normal1).addColor(normal2);
		ArrayList<String> abilities = AbilityConfiguration.config().getAbilityNames();
		StringTable table = new StringTable(color);

		// Add headings:
		table.addLine(new String[]{"ABILITY","USES", "EXP AWARDED"});
		
		// Add data:
		StatisticsManager manager = StatisticsManager.manager();
		for (int i = 0; i < abilities.size(); i++) {
			
			String name = abilities.get(i);
			
			String[] row = new String[]{name, manager.getAbilityUses(name).toString(), manager.getAbilityExp(name).toString()};
			
			table.addLine(row);
			
		}
		
		table.collapse();
		
		StringBook book = new StringBook("abilities", color, 18);
		
		book.addTable(table);
		
		return book.framed(0, table.calcTotalWidth());
		
		
	}
	
	public static String statisticsAge(long milliseconds) {

		
		int seconds = (int) (milliseconds / 1000) % 60 ;
		int minutes = (int) ((milliseconds / (1000*60)) % 60);
		int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
		int days    = (int) ((milliseconds / (1000*60*60*24)));
		
		return positive + "Statistics age " + days + "d " + hours + "h " + minutes + "m " + seconds + "s.";
		
		
	}
	
	public static String xrayIndication(Integer totalPlayers, ArrayList<String> indications) {

		
		ColorCircle color = new ColorCircle().addColor(normal1).addColor(normal2);
		StringBuffer result = new StringBuffer();
		
		// Totals:
		result.append(color.nextColor() + "Indications: " + indications.size() + "/" + totalPlayers);
		
		result.append("\n");
		
		// Players:
		result.append(color.nextColor() + "Indications: ");
		if(indications.size() > 0){
			
			result.append(TextUtil.flatten(indications));
			
		}else{
			
			result.append("none");
			
		}
		
		return TextUtil.frame("x-ray mod indications", color.nextColor(), result.toString(), 60);
		
		
	}

	public static String xray(SagaPlayer sagaPlayer, Integer page) {

		
		String name = sagaPlayer.getName();
		
		ColorCircle color = new ColorCircle().addColor(normal1).addColor(normal2);
		StringBook book = new StringBook(name + " x-ray statistics", color, 10);

		// Blocks:
		StringTable table = new StringTable(color);
		String[] line = new String[2];
		HashSet<Material> xrayMaterials = XrayIndicator.getXrayMaterials();
		
		line = new String[]{"MATERIAL", "AMOUNT", "RATIO"};
		table.addLine(line);
		
		for (Material xrMaterial : xrayMaterials) {
			
			Integer amount = StatisticsManager.manager().getOreMined(name, xrMaterial);
			String amountValue = amount.toString();
			
			Double ratio = StatisticsManager.manager().getOreRatio(name, xrMaterial);
			boolean indication = BalanceConfiguration.config().checkXrayIndication(xrMaterial, ratio);
			
			String ratioValue = String.format("%.2g", ratio*100) + " %";
			if(indication) ratioValue = ChatColor.DARK_RED + ratioValue;
			
			if(xrMaterial == Material.STONE){
				
				line = new String[]{EconomyMessages.material(xrMaterial), amountValue, ""};
				
			}else{
				
				line = new String[]{EconomyMessages.material(xrMaterial), amountValue, ratioValue};
			
			}
			
			table.addLine(line);
			
		}
		
		table.collapse();
		book.addTable(table);
		
		return book.framed(page);
		
		
	}

	public static String exp(int page) {
	
		
		ColorCircle color = new ColorCircle().addColor(normal1).addColor(normal2);
		StringTable table = new StringTable(color);

		// Add headings:
		table.addLine(new String[]{"SKILL","EXP PER USER","TOTAL EXP"});
		
		ArrayList<String> skills = SkillConfiguration.config().getSkillNames();
		for (String skill : skills) {
			
			Double exp = StatisticsManager.manager().getBlockSkillExperience(skill).doubleValue();
			Double players = StatisticsManager.manager().countBlockSkillPlayers(skill).doubleValue();
			
			if(players != 0){
				table.addLine(new String[]{skill, MathUtil.round(exp/players).toString(), exp.toString()});
			}else{
				table.addLine(new String[]{skill, "none", "none"});
			}
			
		}
		
		table.collapse();
		
		StringBook book = new StringBook("experience statistics", color, 10);
		
		book.addTable(table);
		
		return book.framed(page, table.calcTotalWidth());
		
		
	}

	
	// Resetting:
	public static String reset() {
	
		return positive + "Statistics reset.";
		
	}

	
}
