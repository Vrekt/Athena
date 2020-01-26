package athena.account.resource.device;

/**
 * Represents device info.
 */
public final class Device {

    /**
     * Type, ex: "LGE"
     * Model, ex: "LG-H918"
     * OS:, ex: "9.0"
     */
    private String type, model, os;

    public Device(String type, String model, String os) {
        this.type = type;
        this.model = model;
        this.os = os;
    }

    /**
     * @return type/manufacturer
     */
    public String type() {
        return type;
    }

    /**
     * @return device model
     */
    public String model() {
        return model;
    }

    /**
     * @return device OS.
     */
    public String os() {
        return os;
    }
}
