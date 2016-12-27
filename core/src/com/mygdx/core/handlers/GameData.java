package com.mygdx.core.handlers;

import java.io.Serializable;

public class GameData implements Serializable {
	
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
	private boolean brickPurchased;
	private boolean kamehamehaPurchased;
	private boolean fireBallEquiped;
	private boolean excaliburEquiped;
	private boolean brickEquiped;
	private boolean kamehamehaEquiped;
	private int sound;
	private boolean night;
    private String selector;
	private long tentativeScore;
    private boolean soundPause = false;
    private int money = 100;
    private boolean playerBlue = false;
	private boolean playerRed = false;
	private boolean playerYellow = false;
	private boolean playerGreen = false;

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
        money = 100;
		playerBlue = false;
		playerRed = false;
		playerYellow = false;
		playerGreen = false;
		fireBallPurchased =false;
		excaliburPurchased = false;
		kamehamehaPurchased = false;
		brickPurchased = false;
		brickEquiped = false;
		fireBallEquiped = false;
		excaliburEquiped = false;
		kamehamehaEquiped = false;
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
        System.out.println("***HIGHT SCORES***");
        for(int i=0;i<highScores.length;i++)
            System.out.println(highScores[i]);
        return highScores;
    }

	public String[] getNames() { return names; }

	public long getTentativeScore() { return tentativeScore; }

	public void setTenativeScore(int i) {
        tentativeScore = i;
        System.out.println("TENTATIVE SCORE: "+ tentativeScore);
    }

	public long getWingState() { return wings; }

	public void setWingState(int i) { wings = i; }

	public void setNewHighScore(boolean a) {
        newHighScore = a;
        System.out.println("IS HIGH SCORE: "+a);
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

	public boolean isBrickPurchased() {
		return brickPurchased;
	}

	public void setBrickPurchased(boolean brickPurchased) {
		this.brickPurchased = brickPurchased;
	}

	public boolean isBrickEquiped() {
		return brickEquiped;
	}

	public void setBrickEquiped(boolean brickEquiped) {
		this.brickEquiped = brickEquiped;
	}
}

















