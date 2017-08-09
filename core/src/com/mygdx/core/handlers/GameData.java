package com.mygdx.core.handlers;

import com.badlogic.gdx.Gdx;


public class GameData {
	private static String LOG_TAG = GameData.class.getSimpleName();
	
	private static final long serialVersionUID = 1;
	private int wings;
	private final int MAX_SCORES = 1;
	private long[] highScores;
	private String[] names;
	private boolean newHighScore;
    private boolean fullBarPurchased;
    private boolean adsRemoverPurchased;
	private boolean fireBallPurchased;
	private boolean excaliburPurchased;
	private boolean bootPurchased;
	private boolean brick2Purchased;
	private boolean lightningPurchased;
	private boolean megaJumpPurchased;
	private boolean fireBall2Purchased;
	private boolean lightningEquiped;
	private boolean megaJumpEquiped;
	private boolean fireBall2Equiped;
	private boolean kamehamehaPurchased;
	private boolean fireBallEquiped;
	private boolean excaliburEquiped;
	private boolean bootEquiped;
	private boolean brick2Equiped;
	private boolean kamehamehaEquiped;
	private int sound;
	private boolean night;
    private String selector;
	private long tentativeScore;
    private boolean soundPause = false;
    private int money = 0;
    private boolean playerBlue = false;
	private boolean playerRed = false;
	private boolean playerYellow = false;
	private boolean playerGreen = false;
	private boolean isFirstPlay = true;
	private int pickedGamePlay = 2;


	public GameData() {
        selector = "random";
		wings = 1;
		highScores = new long[MAX_SCORES];
		names = new String[MAX_SCORES];
		newHighScore = false;
        fullBarPurchased = false;
        adsRemoverPurchased = false;
        sound = 2;
        night = false;
		playerBlue = false;
		playerRed = false;
		playerYellow = false;
		playerGreen = false;
		fireBallPurchased =false;
		excaliburPurchased = false;
		kamehamehaPurchased = false;
		bootPurchased = false;
		bootEquiped = false;
		fireBallEquiped = false;
		excaliburEquiped = false;
		kamehamehaEquiped = false;
		brick2Purchased = false;
		lightningPurchased = false;
		megaJumpPurchased = false;
		fireBall2Purchased = false;
		brick2Equiped = false;
		lightningEquiped = false;
		megaJumpEquiped = false;
		fireBall2Equiped = false;
		isFirstPlay = true;
	}
	
	// sets up an empty high scores table
	public void init() {
		for(int i = 0; i < MAX_SCORES; i++) {
			highScores[i] = 0;
			names[i] = "---";
		}
	}
	
	public boolean isHighScore(long score) {
		if(score>highScores[0])
			return true;
		else
			return false;
	}
	
	public void addHighScore(long newScore, String name) {
		if(isHighScore(newScore)) {
			highScores[MAX_SCORES - 1] = newScore;
			names[MAX_SCORES - 1] = name;
			sortHighScores();
		}
	}
	
	public void sortHighScores() {
		for(int i = 0; i < MAX_SCORES; i++) {
			long score = highScores[i];
			String name = names[i];
			int j;
			for(j = i - 1;
				j >= 0 && highScores[j] < score;
				j++) {
				highScores[j + 1] = highScores[j];
				names[j + 1] = names[j];
			}
			highScores[j + 1] = score;
			names[j + 1] = name;
		}
	}

	public boolean isSoundPause() {
		return soundPause;
	}

	public void setSoundPause(boolean soundPause) {
		this.soundPause = soundPause;
	}

	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

	public int isSoundEnable() {return sound;}

	public void setSound(int s) {sound = s;}

	public long[] getHighScores() {
        Gdx.app.debug(LOG_TAG,"***HIGHT SCORES***");
        for(int i=0;i<highScores.length;i++)
            Gdx.app.debug(LOG_TAG,"highscore:"+highScores[i]);
        return highScores;
    }

	public String[] getNames() { return names; }

	public long getTentativeScore() { return tentativeScore; }

	public void setTenativeScore(int i) {
        tentativeScore = i;
        Gdx.app.debug(LOG_TAG,"TENTATIVE SCORE: "+ tentativeScore);
    }

	public long getWingState() { return wings; }

	public void setWingState(int i) { wings = i; }

	public void setNewHighScore(boolean a) {
        newHighScore = a;
        Gdx.app.debug(LOG_TAG,"IS HIGH SCORE: "+a);
    }

	public boolean isFireBallEquiped() {
		return fireBallEquiped;
	}

	public void setFireBallEquiped(boolean fireBallEquiped) {
		this.fireBallEquiped = fireBallEquiped;
	}

	public boolean isExcaliburEquiped() {
		return excaliburEquiped;
	}

	public void setExcaliburEquiped(boolean excaliburEquiped) {
		this.excaliburEquiped = excaliburEquiped;
	}

	public boolean isKamehamehaEquiped() {
		return kamehamehaEquiped;
	}

	public void setKamehamehaEquiped(boolean kamehamehaEquiped) {
		this.kamehamehaEquiped = kamehamehaEquiped;
	}

	public boolean isFireBallPurchased() {
		return fireBallPurchased;
	}

	public void setFireBallPurchased(boolean fireBallPurchased) {
		this.fireBallPurchased = fireBallPurchased;
	}

	public boolean isExcaliburPurchased() {
		return excaliburPurchased;
	}

	public void setExcaliburPurchased(boolean excaliburPurchased) {
		this.excaliburPurchased = excaliburPurchased;
	}

	public boolean isKamehamehaPurchased() {
		return kamehamehaPurchased;
	}

	public void setKamehamehaPurchased(boolean kamehamehaPurchased) {
		this.kamehamehaPurchased = kamehamehaPurchased;
	}

	public boolean getNewHighScore() {return newHighScore;}

	public void setAdsRemoverPurchased(boolean a) { adsRemoverPurchased = a; }

	public boolean getAdsRemoverPurchased() { return adsRemoverPurchased; }

	public void setFullBarPurchased(boolean a) { fullBarPurchased = a; }

	public boolean getFullBarPurchased() { return fullBarPurchased; }

	public boolean isNight() {
		return night;
	}

	public void setNight(boolean night) {
		this.night = night;
	}

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public boolean isPlayerBlue() {
        return playerBlue;
    }

    public void setPlayerBlue(boolean playerBlue) {
        this.playerBlue = playerBlue;
    }

    public boolean isPlayerRed() {
        return playerRed;
    }

    public void setPlayerRed(boolean playerRed) {
        this.playerRed = playerRed;
    }

    public boolean isPlayerYellow() {
        return playerYellow;
    }

    public void setPlayerYellow(boolean playerYellow) {
        this.playerYellow = playerYellow;
    }

    public boolean isPlayerGreen() {
        return playerGreen;
    }

    public void setPlayerGreen(boolean playerGreen) {
        this.playerGreen = playerGreen;
    }

	public boolean isBootPurchased() {
		return bootPurchased;
	}

	public void setBootPurchased(boolean bootPurchased) {
		this.bootPurchased = bootPurchased;
	}

	public boolean isBootEquiped() {
		return bootEquiped;
	}

	public void setBootEquiped(boolean bootEquiped) {
		this.bootEquiped = bootEquiped;
	}

	public boolean isBrick2Purchased() {
		return brick2Purchased;
	}

	public void setBrick2Purchased(boolean brick2Purchased) {
		this.brick2Purchased = brick2Purchased;
	}

	public boolean isLightningPurchased() {
		return lightningPurchased;
	}

	public void setLightningPurchased(boolean lightningPurchased) {
		this.lightningPurchased = lightningPurchased;
	}

	public boolean isMegaJumpPurchased() {
		return megaJumpPurchased;
	}

	public void setMegaJumpPurchased(boolean megaJumpPurchased) {
		this.megaJumpPurchased = megaJumpPurchased;
	}

	public boolean isFireBall2Purchased() {
		return fireBall2Purchased;
	}

	public void setFireBall2Purchased(boolean fireBall2Purchased) {
		this.fireBall2Purchased = fireBall2Purchased;
	}

	public boolean isBrick2Equiped() {
		return brick2Equiped;
	}

	public void setBrick2Equiped(boolean brick2Equiped) {
		this.brick2Equiped = brick2Equiped;
	}

	public boolean isLightningEquiped() {
		return lightningEquiped;
	}

	public void setLightningEquiped(boolean lightningEquiped) {
		this.lightningEquiped = lightningEquiped;
	}

	public boolean isMegaJumpEquiped() {
		return megaJumpEquiped;
	}

	public void setMegaJumpEquiped(boolean megaJumpEquiped) {
		this.megaJumpEquiped = megaJumpEquiped;
	}

	public boolean isFireBall2Equiped() {
		return fireBall2Equiped;
	}

	public void setFireBall2Equiped(boolean fireBall2Equiped) {
		this.fireBall2Equiped = fireBall2Equiped;
	}

	public boolean isFirstPlay() {
		return false;
	}

	public void setFirstPlay(boolean firstPlay) {
		isFirstPlay = firstPlay;
	}

	public int getPickedGamePlay() {
		return pickedGamePlay;
	}

	public void setPickedGamePlay(int pickedGamePlay) {
		this.pickedGamePlay = pickedGamePlay;
	}

	/**Empty Data slot just in case. If data added after publish old file should be deleted
	   to avoid previous user data loss keep some empty data slot available.**/
	private int data1 = 1;
	private int data2 = 1;
	private int data3 = 1;
	private int data4 = 1;
	private int data5 = 1;

	private boolean data6 = false;
	private boolean data7 = false;
	private boolean data8 = false;
	private boolean data9 = false;
	private boolean data10 = false;

	private String[] data11;
	private String[] data12;
	private String[] data13;
	private String[] data14;
	private String[] data15;

	public int getData2() {
		return data2;
	}

	public void setData2(int data2) {
		this.data2 = data2;
	}

	public int getData3() {
		return data3;
	}

	public void setData3(int data3) {
		this.data3 = data3;
	}

	public int getData4() {
		return data4;
	}

	public void setData4(int data4) {
		this.data4 = data4;
	}

	public int getData5() {
		return data5;
	}

	public void setData5(int data5) {
		this.data5 = data5;
	}

	public boolean isData6() {
		return data6;
	}

	public void setData6(boolean data6) {
		this.data6 = data6;
	}

	public boolean isData7() {
		return data7;
	}

	public void setData7(boolean data7) {
		this.data7 = data7;
	}

	public boolean isData8() {
		return data8;
	}

	public void setData8(boolean data8) {
		this.data8 = data8;
	}

	public boolean isData9() {
		return data9;
	}

	public void setData9(boolean data9) {
		this.data9 = data9;
	}

	public boolean isData10() {
		return data10;
	}

	public void setData10(boolean data10) {
		this.data10 = data10;
	}

	public String[] getData11() {
		return data11;
	}

	public void setData11(String[] data11) {
		this.data11 = data11;
	}

	public String[] getData12() {
		return data12;
	}

	public void setData12(String[] data12) {
		this.data12 = data12;
	}

	public String[] getData13() {
		return data13;
	}

	public void setData13(String[] data13) {
		this.data13 = data13;
	}

	public String[] getData14() {
		return data14;
	}

	public void setData14(String[] data14) {
		this.data14 = data14;
	}

	public String[] getData15() {
		return data15;
	}

	public void setData15(String[] data15) {
		this.data15 = data15;
	}



}

















