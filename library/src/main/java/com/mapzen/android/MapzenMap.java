package com.mapzen.android;

import com.mapzen.android.model.DebugFlag;
import com.mapzen.android.model.EaseType;
import com.mapzen.android.model.FeaturePickListener;
import com.mapzen.android.model.Marker;
import com.mapzen.android.model.Polygon;
import com.mapzen.android.model.Polyline;
import com.mapzen.android.model.ViewCompleteListener;
import com.mapzen.tangram.LngLat;
import com.mapzen.android.model.MapStyle;
import com.mapzen.tangram.MapController;
import com.mapzen.tangram.MapData;
import com.mapzen.tangram.TouchInput;

import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the main class of the Mapzen Android API and is the entry point for all methods related
 * to the map. You cannot instantiate a {@link MapzenMap} object directly. Rather you must obtain
 * one from {@link MapFragment#getMapAsync(OnMapReadyCallback)} or
 * {@link MapView#getMapAsync(OnMapReadyCallback)}.
 */
public class MapzenMap {

    private final MapController mapController;
    private final OverlayManager overlayManager;

    private TouchInput.TapResponder tapResponder;
    private TouchInput.DoubleTapResponder doubleTapResponder;
    private TouchInput.LongPressResponder longPressResponder;
    private TouchInput.PanResponder panResponder;
    private TouchInput.RotateResponder rotateResponder;
    private TouchInput.ScaleResponder scaleResponder;
    private TouchInput.ShoveResponder shoveResponder;

    private static final HashMap<EaseType, MapController.EaseType>
            EASE_TYPE_TO_MAP_CONTROLLER_EASE_TYPE = new HashMap();

    private static final HashMap<DebugFlag, MapController.DebugFlag>
            DEBUG_FLAG_TO_TANGRAM_DEBUG_FLAG = new HashMap<>();

    static {
        EASE_TYPE_TO_MAP_CONTROLLER_EASE_TYPE.put(EaseType.LINEAR, MapController.EaseType.LINEAR);
        EASE_TYPE_TO_MAP_CONTROLLER_EASE_TYPE.put(EaseType.CUBIC, MapController.EaseType.CUBIC);
        EASE_TYPE_TO_MAP_CONTROLLER_EASE_TYPE.put(EaseType.QUINT, MapController.EaseType.QUINT);
        EASE_TYPE_TO_MAP_CONTROLLER_EASE_TYPE.put(EaseType.SINE, MapController.EaseType.SINE);

        DEBUG_FLAG_TO_TANGRAM_DEBUG_FLAG.put(DebugFlag.FREEZE_TILES,
                MapController.DebugFlag.FREEZE_TILES);
        DEBUG_FLAG_TO_TANGRAM_DEBUG_FLAG.put(DebugFlag.PROXY_COLORS,
                MapController.DebugFlag.PROXY_COLORS);
        DEBUG_FLAG_TO_TANGRAM_DEBUG_FLAG.put(DebugFlag.TILE_BOUNDS,
                MapController.DebugFlag.TILE_BOUNDS);
        DEBUG_FLAG_TO_TANGRAM_DEBUG_FLAG.put(DebugFlag.TILE_INFOS,
                MapController.DebugFlag.TILE_INFOS);
        DEBUG_FLAG_TO_TANGRAM_DEBUG_FLAG.put(DebugFlag.LABELS,
                MapController.DebugFlag.LABELS);
        DEBUG_FLAG_TO_TANGRAM_DEBUG_FLAG.put(DebugFlag.TANGRAM_INFOS,
                MapController.DebugFlag.TANGRAM_INFOS);
        DEBUG_FLAG_TO_TANGRAM_DEBUG_FLAG.put(DebugFlag.ALL_LABELS,
                MapController.DebugFlag.ALL_LABELS);
    }

    /**
     * Creates a new map based on the given {@link MapView} and {@link MapController}.
     */
    MapzenMap(MapController mapController, OverlayManager overlayManager) {
        this.mapController = mapController;
        this.overlayManager = overlayManager;
    }

    /**
     * Provides access to the underlying Tangram {@link MapController}.
     */
    public MapController getMapController() {
        return mapController;
    }

    /**
     * Provides access to the underlying {@link OverlayManager}.
     */
    public OverlayManager getOverlayManager() {
        return overlayManager;
    }

    /**
     * Sets the map's underlying stylesheet.
     * @param mapStyle
     */
    public void setStyle(MapStyle mapStyle) {
        mapController.loadSceneFile(mapStyle.getSceneFile());
    }

    /**
     * Sets map zoom without animation.
     */
    public void setZoom(float zoom) {
        mapController.setZoom(zoom);
    }

    /**
     * Sets map zoom with animation and default, {@code EaseType.CUBIC} easing.
     *
     * @param zoom map zoom level
     * @param duration animation duration in millis
     */
    public void setZoom(float zoom, int duration) {
        mapController.setZoomEased(zoom, duration);
    }

    /**
     * Sets map zoom with animation and easing.
     *
     * @param zoom map zoom level
     * @param duration animation duration in millis
     * @param easeType map ease type
     */
    public void setZoom(float zoom, int duration, EaseType easeType) {
        mapController.setZoomEased(zoom, duration, EASE_TYPE_TO_MAP_CONTROLLER_EASE_TYPE.get(
                easeType));
    }

    /**
     * Returns map zoom.
     */
    public float getZoom() {
        return mapController.getZoom();
    }

    /**
     * Set map position without animation.
     */
    public void setPosition(LngLat lngLat) {
        mapController.setPosition(lngLat);
    }

    /**
     * Set map position with animation and default {@code EaseType.CUBIC} easing.
     *
     * @param lngLat position to center map on
     * @param duration animation duration in millis
     */
    public void setPosition(LngLat lngLat, int duration) {
        mapController.setPositionEased(lngLat, duration);
    }

    /**
     * Set map position with animation and easing.
     *
     * @param lngLat position to center map on
     * @param duration animation duration in millis
     * @param easeType map ease type
     */
    public void setPosition(LngLat lngLat, int duration, EaseType easeType) {
        mapController.setPositionEased(lngLat, duration, EASE_TYPE_TO_MAP_CONTROLLER_EASE_TYPE.get(
                easeType));
    }

    /**
     * Returns map position.
     */
    public LngLat getPosition() {
        return mapController.getPosition();
    }

    /**
     * Set map rotation in radians without animation.
     */
    public void setRotation(float radians) {
        mapController.setRotation(radians);
    }

    /**
     * Set map rotation in radians with animation and default {@code EaseType.CUBIC} easing.
     *
     * @param radians map rotation
     * @param duration animation duration in millis
     */
    public void setRotation(float radians, int duration) {
        mapController.setRotationEased(radians, duration);
    }

    /**
     * Set map rotation in radians with animation and custom easing.
     *
     * @param radians map rotation
     * @param duration animation duration in millis
     * @param easeType map ease type
     */
    public void setRotation(float radians, int duration, EaseType easeType) {
        mapController.setRotationEased(radians, duration, EASE_TYPE_TO_MAP_CONTROLLER_EASE_TYPE.get(
                easeType));
    }

    /**
     * Get map rotation in radians.
     */
    public float getRotation() {
        return mapController.getRotation();
    }

    /**
     * Set map tilt in radians without animation.
     *
     * @param radians tilt in radians
     */
    public void setTilt(float radians) {
        mapController.setTilt(radians);
    }

    /**
     * Set map tilt in radians with animation and default {@code EaseType.CUBIC} easing.
     *
     * @param radians tilt in radians
     * @param duration duration in millis
     */
    public void setTilt(float radians, int duration) {
        mapController.setTiltEased(radians, duration);
    }

    /**
     * Set map tilt in radians with animation and custom easing.
     *
     * @param radians tilt in radians
     * @param duration duration in millis
     * @param easeType map ease type
     */
    public void setTilt(float radians, int duration, EaseType easeType) {
        mapController.setTiltEased(radians, duration, EASE_TYPE_TO_MAP_CONTROLLER_EASE_TYPE.get(
                easeType));
    }

    /**
     * Get map tilt in radians.
     */
    public float getTilt() {
        return mapController.getTilt();
    }

    /**
     * When enabled, shows icon on map to allow centering map on current location. While
     * enabled, the user's current location will be updated in the background. When disabled, the
     * icon is hidden from the map.
     */
    public void setMyLocationEnabled(boolean enabled) {
        overlayManager.setMyLocationEnabled(enabled);
    }

    /**
     * Are we tracking the user's current location.
     * @return
     */
    public boolean isMyLocationEnabled() {
        return overlayManager.isMyLocationEnabled();
    }

    /**
     * Set an external click listener to be invoked after the internal listener.
     * @param listener
     */
    public void setFindMeOnClickListener(View.OnClickListener listener) {
        overlayManager.setFindMeOnClickListener(listener);
    }

    /**
     * Adds marker overlay to map. Returned {@link MapData} should be removed from map
     */
    public MapData addMarker(Marker marker) {
        return overlayManager.addMarker(marker);
    }

    /**
     * Adds polyline overlay to map.
     */
    public MapData addPolyline(Polyline polyline) {
        return overlayManager.addPolyline(polyline);
    }

    /**
     * Adds polygon overlay to map.
     */
    public MapData addPolygon(Polygon polygon) {
        return overlayManager.addPolygon(polygon);
    }

    /**
     * Find the geographic coordinates corresponding to the given position on screen.
     *
     * @param screenX Pixels from the left edge of the screen
     * @param screenY Pixels from the top edge of the screen
     * @return LngLat corresponding to the given point
     */
    public LngLat coordinatesAtScreenPosition(double screenX, double screenY) {
        return mapController.coordinatesAtScreenPosition(screenX, screenY);
    }

    /**
     * Set tap responder for tap gestures on map.
     */
    public void setTapResponder(final TouchInput.TapResponder tapResponder) {
        this.tapResponder = tapResponder;
        mapController.setTapResponder(this.tapResponder);
    }

    /**
     * Get the map's tap responder.
     */
    public TouchInput.TapResponder getTapResponder() {
        return tapResponder;
    }

    /**
     * Set double tap responder for tap gestures on map.
     */
    public void setDoubleTapResponder(TouchInput.DoubleTapResponder doubleTapResponder) {
        this.doubleTapResponder = doubleTapResponder;
        mapController.setDoubleTapResponder(this.doubleTapResponder);
    }

    /**
     * Get the map's double tap responder.
     */
    public TouchInput.DoubleTapResponder getDoubleTapResponder() {
        return doubleTapResponder;
    }

    /**
     * Set long press responder for tap gestures on map.
     */
    public void setLongPressResponder(TouchInput.LongPressResponder longPressResponder) {
        this.longPressResponder = longPressResponder;
        mapController.setLongPressResponder(this.longPressResponder);
    }

    /**
     * Get the map's long press responder.
     */
    public TouchInput.LongPressResponder getLongPressResponder() {
        return longPressResponder;
    }

    /**
     * Set pan responder for tap gestures on map.
     */
    public void setPanResponder(TouchInput.PanResponder panResponder) {
        this.panResponder = panResponder;
        mapController.setPanResponder(this.panResponder);
    }

    /**
     * Get the map's pan responder.
     */
    public TouchInput.PanResponder getPanResponder() {
        return panResponder;
    }

    /**
     * Set rotate responder for tap gestures on map.
     */
    public void setRotateResponder(TouchInput.RotateResponder rotateResponder) {
        this.rotateResponder = rotateResponder;
        mapController.setRotateResponder(this.rotateResponder);
    }

    /**
     * Get the map's rotate responder.
     */
    public TouchInput.RotateResponder getRotateResponder() {
        return rotateResponder;
    }

    /**
     * Set scale responder for tap gestures on map.
     */
    public void setScaleResponder(TouchInput.ScaleResponder scaleResponder) {
        this.scaleResponder = scaleResponder;
        mapController.setScaleResponder(scaleResponder);
    }

    /**
     * Get the map's scale responder.
     */
    public TouchInput.ScaleResponder getScaleResponder() {
        return scaleResponder;
    }

    /**
     * Set shove responder for tap gestures on map.
     */
    public void setShoveResponder(TouchInput.ShoveResponder shoveResponder) {
        this.shoveResponder = shoveResponder;
        mapController.setShoveResponder(this.shoveResponder);
    }

    /**
     * Get the map's shove responder.
     */
    public TouchInput.ShoveResponder getShoveResponder() {
        return shoveResponder;
    }

    /**
     * Set whether the gesture {@code second} can be recognized while {@code first} is in progress.
     *
     * @param first Initial gesture type
     * @param second Subsequent gesture type
     * @param allowed True if {@code second} should be recognized, else false
     */
    public void setSimultaneousGestureAllowed(TouchInput.Gestures first, TouchInput.Gestures second,
            boolean allowed) {
        mapController.setSimultaneousGestureAllowed(first, second, allowed);
    }

    /**
     * Get whether the gesture {@code second} can be recognized while {@code first} is in progress.
     *
     * @param first Initial gesture type
     * @param second Subsequent gesture type
     * @return True if {@code second} will be recognized, else false
     */
    public boolean isSimultaneousGestureAllowed(TouchInput.Gestures first,
            TouchInput.Gestures second) {
        return mapController.isSimultaneousGestureAllowed(first, second);
    }

    /**
     * Set a listener for feature pick events.
     * @param listener Listener to call
     */
    public void setFeaturePickListener(final FeaturePickListener listener) {
        mapController.setFeaturePickListener(new MapController.FeaturePickListener() {
            @Override public void onFeaturePick(Map<String, String> properties, float positionX,
                    float positionY) {
                listener.onFeaturePick(properties, positionX, positionY);
            }
        });
    }

    /**
     * Query the map for labeled features at the given screen coordinates; results will be returned
     * in a callback to the object set by {@link #setFeaturePickListener(FeaturePickListener)}.
     * @param posX The horizontal screen coordinate
     * @param posY The vertical screen coordinate
     */
    public void pickFeature(float posX, float posY) {
        mapController.pickFeature(posX, posY);
    }

    /**
     * Set a listener for when view is fully loaded and no ease or label animations running.
     */
    public void setViewCompleteListener(final ViewCompleteListener listener) {
        mapController.setViewCompleteListener(new MapController.ViewCompleteListener() {
            @Override public void onViewComplete() {
                listener.onViewComplete();
            }
        });
    }

    /**
     * Enqueue a Runnable to be executed synchronously on the rendering thread.
     * @param r Runnable to run
     */
    public void queueEvent(Runnable r) {
        mapController.queueEvent(r);
    }

    /**
     * Make a debugging feature active or inactive.
     * @param flag The feature to set
     * @param on True to activate the feature, false to deactivate
     */
    public void setDebugFlag(DebugFlag flag, boolean on) {
        mapController.setDebugFlag(DEBUG_FLAG_TO_TANGRAM_DEBUG_FLAG.get(flag), on);
    }

    /**
     * Enqueue a scene component update with its corresponding YAML node value.
     * @param componentPath The YAML component path delimited by a '.' (example "scene.animated")
     * @param value A YAML valid string (example "{ property: true }" or "true")
     */
    public void queueSceneUpdate(String componentPath, String value) {
        mapController.queueSceneUpdate(componentPath, value);
    }

    /**
     * Apply updates queued by queueSceneUpdate; this empties the current queue of updates.
     */
    public void applySceneUpdates() {
        mapController.applySceneUpdates();
    }

    /**
     * Draws two pins on the map. The start pin is active and the end pin is inactive.
     * @param start
     * @param end
     */
    public void drawRoutePins(LngLat start, LngLat end) {
        overlayManager.drawRoutePins(start, end);
    }

    /**
     * Clears the start and end pins from the map.
     */
    public void clearRoutePins() {
        overlayManager.clearRoutePins();
    }

    /**
     * Draws a dropped pin on the map at the point supplied.
     * @param point
     */
    public void drawDroppedPin(LngLat point) {
        overlayManager.drawDroppedPin(point);
    }

    /**
     * Clears the dropped pin from the map.
     */
    public void clearDroppedPins() {
        overlayManager.clearDroppedPin();
    }

    /**
     * Draws a search result on the map at the point supplied. The pin will be active.
     * @param point
     */
    public void drawSearchResult(LngLat point) {
        drawSearchResult(point, true);
    }

    /**
     * Draws a search result on the map at the point supplied.
     * @param point
     * @param active
     */
    public void drawSearchResult(LngLat point, boolean active) {
        overlayManager.drawSearchResult(point, active);
    }

    /**
     * Draws search results on the map. All pins are displayed as active.
     * @param points
     */
    public void drawSearchResults(List<LngLat> points) {
        int index = 0;
        for (LngLat point : points) {
            overlayManager.drawSearchResult(point, true, index);
            index++;
        }
    }

    /**
     * Draws search results on the map. All pins will be inactive except for the one at the active
     * index supplied.
     * @param points
     * @param activeIndexes
     */
    public void drawSearchResults(List<LngLat> points, int... activeIndexes) {
        int index = 0;
        for (LngLat point : points) {
            boolean status = false;
            for (int i = 0; i < activeIndexes.length; i++) {
                if (activeIndexes[i] == index) {
                    status = true;
                    break;
                }
            }
            overlayManager.drawSearchResult(point, status, index);
            index++;
        }
    }

    /**
     * Clears search results from the map.
     */
    public void clearSearchResults() {
        overlayManager.clearSearchResult();
    }

    /**
     * Draws route pin at the point supplied.
     * @param point
     */
    public void drawRouteLocationMarker(LngLat point) {
        overlayManager.drawRouteLocationMarker(point);
    }

    /**
     * Clears route pin from the map.
     */
    public void clearRouteLocationMarker() {
        overlayManager.clearRouteLocationMarker();
    }

    /**
     * Draws route line on the map for the points supplied.
     * @param points
     */
    public void drawRouteLine(List<LngLat> points) {
        overlayManager.drawRouteLine(points);
    }

    /**
     * Clears route line from the map.
     */
    public void clearRouteLine() {
        overlayManager.clearRouteLine();
    }

}
