package com.mygdx.core.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.badlogic.gdx.Gdx;

public class Save {

	private static String LOG_TAG = Save.class.getSimpleName();
	
	public static GameData gd;
	public static String path = Gdx.files.getLocalStoragePath()+ "data.sav";
	
	public static void save() {

	}
	
	public static void load() {

	}
	
	public static boolean saveFileExists() {
		return true;
	}
	
	public static void init() {
        Gdx.app.debug(LOG_TAG,"INIT GAME DATA...");
		gd = new GameData();
		gd.init();
		save();
	}
	
}

