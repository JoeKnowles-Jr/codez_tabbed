package com.jk.codez.ad

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jk.codez.CodezViewModel
import com.jk.codez.MainActivity
import com.jk.codez.R
import com.jk.codez.item.Item
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.Layer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

class DialogManager(act: MainActivity) {

    private val activity: MainActivity = act
    private lateinit var resources: Resources
    private var viewModel: CodezViewModel? = null
    private var droppedMarkerLayer: Layer?= null
    private var mLocationManager: LocationManager? = null

    fun initialize(vm: CodezViewModel, res: Resources) {
        viewModel = vm
        resources = res
    }

    @SuppressLint("MissingPermission")
    fun showItemDialog(item: Item?, remote: Boolean) {
        val i: Item = item ?: Item()
        val builder = AestheticDialog.Builder(activity, viewModel, DialogStyle.EDIT)
        val l: Location? = getCurrentLocation()
        if (item == null && l != null) {
            i.setLat(l.latitude)
            i.setLng(l.longitude)
        }
        builder.setItem(i)
        builder.setIsEdit(item != null)
        builder.setButtonClickListener(object : ButtonClickListener {
            override fun onSave(dialog: AestheticDialog.Builder) {
                if (item == null) {
                    if (remote)
                        viewModel?.addRemoteItem(dialog)
                    else
                        viewModel?.addLocalItem(dialog)
                } else {
//                    viewModel.editCode(dialog)
                }
            }

            override fun onCancel(dialog: AestheticDialog.Builder) {

            }

            override fun onDelete(dialog: AestheticDialog.Builder) {
//                viewModel.deleteCode(dialog)
            }
        })
        builder.setAnimation(DialogAnimation.FADE)
        val v = builder.getLayout()
        val mMapView: MapView = v.findViewById(R.id.mv_edit)
        val selectLocationButton = v.findViewById<Button>(R.id.move_pin_set)
        mMapView.getMapAsync { map: MapboxMap ->
            map.setStyle(
                Style.MAPBOX_STREETS
            ) { style: Style ->
                val mLocationComponent = map.locationComponent
                val lco =
                    LocationComponentOptions.builder(activity)
                        .pulseEnabled(true)
                        .build()
                mLocationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(activity, style)
                        .locationComponentOptions(lco)
                        .build()
                )
                mLocationComponent.isLocationComponentEnabled = true
                mLocationComponent.cameraMode = CameraMode.TRACKING
                mLocationComponent.renderMode = RenderMode.COMPASS

                val hoveringMarker = ImageView(activity)
                hoveringMarker.setImageResource(R.drawable.red_marker)
                val params = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER
                )
                hoveringMarker.layoutParams = params
                mMapView.addView(hoveringMarker)

                initDroppedMarker(style)
                if (item?.getLat() != null) {
                    map.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.Builder()
                                .target(LatLng(item.getLat()!!, item.getLng()!!))
                                .zoom(17.0)
                                .build()
                        ), 1000
                    )
                }

                selectLocationButton.setOnClickListener {
                    if (hoveringMarker.visibility == View.VISIBLE) {

                        val mapTargetLatLng: LatLng? = map.cameraPosition.target

                        if (mapTargetLatLng != null) {
                            builder.setPosition(mapTargetLatLng)
                        }

                        hoveringMarker.visibility = View.INVISIBLE

                        selectLocationButton.setBackgroundColor(
                            ContextCompat.getColor(activity, R.color.blue_700)
                        )
                        selectLocationButton.text = activity.getString(R.string.place_pin)

                        if (style.getLayer(DROPPED_MARKER_LAYER_ID) != null) {
                            val source =
                                style.getSourceAs<GeoJsonSource>("dropped-marker-source-id")
                            if (mapTargetLatLng != null) {
                                source?.setGeoJson(
                                    Point.fromLngLat(
                                        mapTargetLatLng.longitude,
                                        mapTargetLatLng.latitude
                                    )
                                )
                            }
                            droppedMarkerLayer =
                                style.getLayer(DROPPED_MARKER_LAYER_ID)
                            droppedMarkerLayer?.setProperties(
                                PropertyFactory.visibility(
                                    Property.VISIBLE
                                )
                            )
                        }

//                    reverseGeocode(Point.fromLngLat(mapTargetLatLng.getLongitude(), mapTargetLatLng.getLatitude()));
                    } else {

                        selectLocationButton.setBackgroundColor(
                            ContextCompat.getColor(
                                activity,
                                R.color.dark_background
                            )
                        )
                        selectLocationButton.text = activity.getString(R.string.set_pin)

                        hoveringMarker.visibility = View.VISIBLE

                        droppedMarkerLayer =
                            style.getLayer(DROPPED_MARKER_LAYER_ID)
                        droppedMarkerLayer?.setProperties(PropertyFactory.visibility(Property.NONE))
                    }
                }
            }
        }
        builder.show()
    }

    fun showConnectionDialog() {
        val builder = AestheticDialog.Builder(activity, viewModel, DialogStyle.CONNECT)
        builder.setButtonClickListener(object: ButtonClickListener {
            override fun onSave(dialog: AestheticDialog.Builder) {

            }

            override fun onCancel(dialog: AestheticDialog.Builder) {

            }

            override fun onDelete(dialog: AestheticDialog.Builder) {

            }

        })
        builder.setAnimation(DialogAnimation.FADE)
        builder.show()

    }

    private fun getLM(): LocationManager? {
        if (mLocationManager == null) mLocationManager = activity.getSystemService(
            AppCompatActivity.LOCATION_SERVICE
        ) as LocationManager?
        return mLocationManager
    }

    private fun getCurrentLocation(): Location? {
        val criteria = Criteria()
        val bestProvider: String = getLM()?.getBestProvider(criteria, true).toString()
        when {
            ContextCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
                    PackageManager.PERMISSION_GRANTED -> {
                val location: Location? = getLM()?.getLastKnownLocation(bestProvider)
                if (location == null) {
                    getLM()?.requestLocationUpdates(bestProvider, 1000, 0f, activity)
                }
                return location
            }
            activity.shouldShowRequestPermissionRationale("ACCESS_FINE_LOCATION") -> {
                println("should request")
            }
            else -> {
                activity.requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
        return null
    }

    private fun initDroppedMarker(loadedMapStyle: Style) {
        loadedMapStyle.addImage(
            "dropped-icon-image", BitmapFactory.decodeResource(
                resources, R.drawable.green_marker
            )
        )
        loadedMapStyle.addSource(GeoJsonSource("dropped-marker-source-id"))
        loadedMapStyle.addLayer(
            SymbolLayer(
                DROPPED_MARKER_LAYER_ID,
                "dropped-marker-source-id"
            ).withProperties(
                PropertyFactory.iconImage("dropped-icon-image"),
                PropertyFactory.visibility(Property.NONE),
                PropertyFactory.iconAllowOverlap(true),
                PropertyFactory.iconIgnorePlacement(true)
            )
        )
    }
    companion object {
        private const val DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID"
    }
}