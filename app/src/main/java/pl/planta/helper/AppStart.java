package pl.planta.helper;

/**
 * public enum AppStart
 * It is used to create variables that can take only certain predetermined values:
 * params FIRST_TIME - for app's first time run
 * params FIRST_TIME_VERSIOB - for app's first time run when the new app's code is greather than last one
 * params NORMAL - for app's in normal activity
 */
public enum AppStart {
    FIRST_TIME(),
    FIRST_TIME_VERSION(),
    NORMAL()
}
