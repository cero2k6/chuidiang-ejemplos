package com.chuidiang.ejemplos.worldwind;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.avlist.AVListImpl;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.symbology.BasicTacticalGraphicAttributes;
import gov.nasa.worldwind.symbology.BasicTacticalSymbolAttributes;
import gov.nasa.worldwind.symbology.SymbologyConstants;
import gov.nasa.worldwind.symbology.TacticalGraphic;
import gov.nasa.worldwind.symbology.TacticalGraphicAttributes;
import gov.nasa.worldwind.symbology.TacticalGraphicUtil;
import gov.nasa.worldwind.symbology.TacticalSymbolAttributes;
import gov.nasa.worldwind.symbology.milstd2525.MilStd2525GraphicFactory;
import gov.nasa.worldwind.symbology.milstd2525.MilStd2525TacticalGraphic;
import gov.nasa.worldwind.symbology.milstd2525.MilStd2525TacticalSymbol;
import gov.nasa.worldwind.symbology.milstd2525.graphics.lines.LinearTarget;

import java.awt.Font;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SimbolosAPP6 {
	private static List<Renderable> listaSimbolos;
	private static double latitudInicial = 36.0;
	private static double longitudInicial = -6.0;
	private static TacticalSymbolAttributes normalAttributes = null;
	private static String[] codigos = {"SHGPUCFRMS----G", "SFAPMFQM------A", "SNGPEWXH-------"};
	public static Iterable<Renderable> getSimbolos(int numSimbolos) {
		listaSimbolos = new LinkedList<>();

		if (null == normalAttributes) {
			inicializaNormalAttributes();
		}

		for (int i = 0; i < numSimbolos; i++) {
			AVList modifiers = new AVListImpl();
			double angulo = Math.random() * 360;
			modifiers.setValue(SymbologyConstants.DIRECTION_OF_MOVEMENT,
					Angle.fromDegrees(angulo));
			modifiers.setValue(SymbologyConstants.ECHELON,
					SymbologyConstants.ECHELON_TEAM_CREW);
			MilStd2525TacticalSymbol simbolo = new MilStd2525TacticalSymbol(
					codigos[i % (codigos.length)], Position.fromDegrees(
							latitudInicial + Math.random(), longitudInicial
									+ Math.random()), modifiers);
			listaSimbolos.add(simbolo);
			TacticalSymbolAttributes att = new BasicTacticalSymbolAttributes();
			att.setScale(0.3);
			simbolo.setAttributes(normalAttributes);
			simbolo.setValue("velocidad", Math.random());
			simbolo.setValue("direccion", angulo);
			simbolo.setValue(AVKey.DISPLAY_NAME, simbolo.getIdentifier());
			simbolo.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
		}

		return listaSimbolos;
	}

	public static void mueveSymbols() {
		for (Renderable simbolo : listaSimbolos) {
			MilStd2525TacticalSymbol app6 = (MilStd2525TacticalSymbol) simbolo;
			double velocidad = (Double) app6.getValue("velocidad");
			double direccion = (Double) app6.getValue("direccion");
			Position posicionActual = app6.getPosition();
			double angulo = direccion + Math.random() / 100.0;
			Position antiguaPoscion = app6.getPosition();
			Position nuevaPosicion = new Position(
					Position.greatCircleEndPosition(posicionActual,
							Angle.fromDegrees(angulo),
							Angle.fromDegrees(velocidad * 0.01)), 200);
			app6.setModifier(SymbologyConstants.DIRECTION_OF_MOVEMENT,
					Angle.fromDegrees(angulo));
			app6.setValue("direccion", angulo);
			app6.moveTo(nuevaPosicion);
			app6.firePropertyChange(AVKey.POSITION, antiguaPoscion, nuevaPosicion);
		}

	}

	private static void inicializaNormalAttributes() {
		normalAttributes = new BasicTacticalSymbolAttributes();
		normalAttributes.setScale(0.2);
		normalAttributes.setOpacity(1.0);
		normalAttributes.setTextModifierFont(Font.decode("Arial-Bold-12"));
		normalAttributes.setTextModifierMaterial(Material.RED);
	}

	public static TacticalGraphic getTacticalGraphic() {
		MilStd2525GraphicFactory factory = new MilStd2525GraphicFactory();
		// We need to create a position list that specifies the three control points in order
		List<Position> positions = Arrays.asList(
		    Position.fromDegrees(42.8073, -6.8180, 0), // PT. 1
		    Position.fromDegrees(42.4845, -6.8495, 0), // PT. 2
		    Position.fromDegrees(42.8686, -6.5088, 0)); // PT. 3
		 
		// And now we can create the graphic
		TacticalGraphic graphic = factory.createGraphic("GFGPSLA----AUSX",positions,null);
		BasicTacticalGraphicAttributes att = new BasicTacticalGraphicAttributes();
		att.setOutlineMaterial(Material.GREEN);
		graphic.setText("un texto");
		graphic.setAttributes(att);
		
		return graphic;
		
	}
}
