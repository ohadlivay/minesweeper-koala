package main.java.controller;

import main.java.view.overlays.*;

public class OverlayController {
    private static OverlayController instance;
    private final NavigationController nav;

    private OverlayController(NavigationController nav) {
        this.nav = nav;
    }

    public static OverlayController getInstance(NavigationController nav) {
        if (instance == null) {
            instance = new OverlayController(nav);
        }
        return instance;
    }

    public static OverlayController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("OverlayController not initialized!");
        }
        return instance;
    }

    public void showOverlay(OverlayType type) {
        OverlayView overlay = getOverlay(type);
        if (overlay != null) {
            overlay.open();
        }
    }

    //method to choose the current overlay based on the enum types
        private OverlayView getOverlay(OverlayType type) {
            OverlayView overlay = null;
            if (type == OverlayType.SETTINGS) {
                overlay = new SettingsOverlay(nav);
            }
            if (type == OverlayType.ADD_EDIT_QUESTION) {
                overlay = new NYI_AddEditQuestionOverlay(nav);
            }
            if (type == OverlayType.VIEW_QUESTION) {
                overlay = new ViewQuestionOverlay(nav);
            }
            if (type == OverlayType.INSTRUCTIONS) {
                overlay = new NYI_GameInstructionOverlay(nav);
            }
            return overlay;
        }

    }


