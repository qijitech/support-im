package support.im.feature.photodraweeview.widget;


public interface OnScaleChangeListener {
    /**
     * Callback for when the scale changes
     *
     * @param scaleFactor the scale factor (<1 for zoom out, >1 for zoom in)
     * @param focusX focal point X position
     * @param focusY focal point Y position
     */
    void onScaleChange(float scaleFactor, float focusX, float focusY);
}
