package controller;

import model.SoftwareModel;

/**
 * Abstract base class for controllers in the application.
 * This class provides a common structure for all controllers,
 * including a reference to the application's software model.
 */
public abstract class Controller {
    // Instance of the software model used by the controller
    SoftwareModel app = new SoftwareModel();
}
