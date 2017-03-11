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
		try {
            Gdx.app.debug(LOG_TAG,"SAVE FILE: "+path);
			ObjectOutputStream out = new ObjectOutputStream(
				new FileOutputStream(path)
			);
			out.writeObject(gd);
			out.close();
		}
		catch(Exception e) {
			Gdx.app.error(LOG_TAG,"error while saving file",e);
		}
	}
	
	public static void load() {

		try {
			if(!saveFileExists()) {
                Gdx.app.debug(LOG_TAG,"DATA FILE DO NOT EXIST: "+path);
				init();
				return;
			}else {
                Gdx.app.debug(LOG_TAG,"FILE EXIST LOAD... "+path);
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
                gd = (GameData) in.readObject();
                in.close();
            }
		}
		catch(Exception e) {
			Gdx.app.error(LOG_TAG,"error while loading file",e);

			Gdx.app.exit();
		}
	}
	
	public static boolean saveFileExists() {
		File f = new File(path);
		return f.exists();
	}
	
	public static void init() {
        Gdx.app.debug(LOG_TAG,"INIT GAME DATA...");
		gd = new GameData();
		gd.init();
		save();
	}
	
}

