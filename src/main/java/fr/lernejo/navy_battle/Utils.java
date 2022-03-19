package fr.lernejo.navy_battle;

import java.util.Map;

public class Utils {
    public void PrintInfo(Map<String, String> gameContext, String Mess) {
        System.out.println("mess : " + Mess);
        System.out.println("my ID:" + gameContext.get("my_id"));
        System.out.println("my port:" + gameContext.get("my_port"));
        System.out.println("adv ID:" + gameContext.get("adv_id"));
        System.out.println("adv URL:" + gameContext.get("adv_url"));
    }

    public String getCharForNumber(int i) {
        System.out.println("x=" + i);
        return (i > 0 && i < 26) ? String.valueOf((char) (i + 64)) : null;
    }
}
