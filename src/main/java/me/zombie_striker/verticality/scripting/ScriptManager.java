package me.zombie_striker.verticality.scripting;

import me.zombie_striker.verticality.scripting.event.IScriptEvent;
import me.zombie_striker.verticality.scripting.script.Script;

import java.util.HashMap;

public class ScriptManager {

    private final HashMap<String, IScriptEvent> eventTypes = new HashMap<>();
    private final HashMap<String, Script> scripts = new HashMap<>();

    public ScriptManager() {

    }

    public void loadScript() {

    }

    public void registerEventType(Class<? extends IScriptEvent> event) {

    }

    public IEventResult callEvent() {
        return null;
    }

    public Script parseScript() {
        return null;
    }

    public void generateScript() {

    }
}
