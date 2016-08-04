package com.mygdx.core.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.badlogic.gdx.Gdx;

public class Save {
	
	public static GameData gd;
	public static String path = Gdx.files.getLocalStoragePath()+ "data.sav";
	
	public static void save() {

		try {
            System.out.println("SAVE FILE: "+path);
			ObjectOutputStream out = new ObjectOutputStream(
				new FileOutputStream(path)
			);
			out.writeObject(gd);
			out.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			//Gdx.app.exit();
		}
	}
	
	public static void load() {

		try {
			if(!saveFileExists()) {
                System.out.println("DATA FILE DO NOT EXIST: "+path);
				init();
				return;
			}else {
                System.out.println("FILE EXIST LOAD... "+path);
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
                gd = (GameData) in.readObject();
                in.close();
            }
		}
		catch(Exception e) {
			e.printStackTrace();
			Gdx.app.exit();
		}
	}
	
	public static boolean saveFileExists() {
		File f = new File(path);
		return f.exists();
	}
	
	public static void init() {
        System.out.println("INIT GAME DATA...");
		gd = new GameData();
		gd.init();
		save();
	}
	
}

