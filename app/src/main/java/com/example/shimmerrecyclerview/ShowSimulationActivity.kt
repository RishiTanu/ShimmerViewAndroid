package com.example.practice.main


import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.practice.R
import com.example.practice.TouchableMapFragment
import com.example.practice.databinding.ActivityShowSimulationBinding
import com.example.practice.utils.CustomTileLayer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.maps.android.SphericalUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


@AndroidEntryPoint
class ShowSimulationActivity : AppCompatActivity() {

    private val viewModel: MapViewModel by viewModels()

    private lateinit var mapView: GoogleMap
    private val markerRouteMap = mutableMapOf<Marker, Long>()
    private val polylineMap = mutableMapOf<Polyline, Int>()
    private var currentDateTime: ZonedDateTime? = null
    private var pageNo: Int = 1
    private var keyRouteId: Long = 0L

    private val updateTimeJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + updateTimeJob)
    private var apiJob: Job? = null
    private var binding : ActivityShowSimulationBinding? = null

    private var legLatLngGlobalRouteCopy: MutableList<RouteList> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowSimulationBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            openMap()
            viewModel.onTriggerEvent(LegEvents.GetGlobalROutes(1))
            //   viewModel.onTriggerEvent(MapEvents.GetGlobalAirSpaceData(keyRouteId.toInt()))
            subscribeObservers()
        }

        binding.apply {

            binding?.resetButton?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Checkbox is checked, hide all polylines
                    clearAllPolylines()
                } else {
                    // Checkbox is unchecked, redraw all polylines
                    drawAllPolylines()
                }
            }

            /* switchSimulationButton.setOnCheckedChangeListener { _, isChecked ->
                 if (isChecked) {
                     //active mode
                     clearActiveModeOnMap()
                     viewModel.resetActiveState()

                     viewModel.onTriggerEvent(MapEvents.GetOwnPlanDataActive(keyRouteId.toInt()))
                     activeButtonOn()
                     collisionChecked.isChecked = true
                     nearByChecked.isChecked = true
                     safeChecked.isChecked = true
                 } else {
                     //simulation mode
                     collisionChecked.isChecked = false
                     nearByChecked.isChecked = false
                     safeChecked.isChecked = false
                     activeButtonOff()
                     clearSimulationModeOnMap()
                     viewModel.resetSimulationState()
                     viewModel.onTriggerEvent(MapEvents.GetGlobalAirSpaceData(keyRouteId.toInt()))
                 }
             }*/

            /*collisionChecked.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    onVeryNearFlightsCheckboxChanged(true)
                } else {
                    onVeryNearFlightsCheckboxChanged(false)
                }
            }

            nearByChecked.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    onSafeZoneFlightsCheckboxChanged(true)
                } else {
                    onSafeZoneFlightsCheckboxChanged(false)
                }
            }

            safeChecked.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    onVerySafeZoneFlightsCheckboxChanged(true)
                } else {
                    onVerySafeZoneFlightsCheckboxChanged(false)
                }
            }*/
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        apiJob?.cancel()
    }


    private val aircraftMarkers = mutableMapOf<String, Marker>()
    var showVeryNearFlights = true
    var showSafeZoneFlights = true
    var showVerySafeZoneFlights = true

    private fun onVeryNearFlightsCheckboxChanged(isChecked: Boolean) {
        showVeryNearFlights = isChecked
        //  updateAircraftMarkers()
    }

    private fun onSafeZoneFlightsCheckboxChanged(isChecked: Boolean) {
        showSafeZoneFlights = isChecked
        //updateAircraftMarkers()
    }

    private fun onVerySafeZoneFlightsCheckboxChanged(isChecked: Boolean) {
        showVerySafeZoneFlights = isChecked
        ///updateAircraftMarkers()
    }


    private suspend fun repeatApiCall() {
        while (isActive) {
            try {
                pageNo++
                fetchAircraftDetails()
            } catch (e: Exception) {
                //handleError(e)
            }
            delay(1000) // Delay for 5 seconds
        }
    }

    private suspend fun fetchAircraftDetails() {
        return withContext(Dispatchers.IO) {
            /*viewModel.onTriggerEvent(
               *//* MapEvents.GetActiveFlightDetail(
                    pageNo = pageNo,
                    lat = 25.8000797854715,
                    lng = -80.2793641534071
                )*//*
            )*/
        }
    }

    private fun openMap() {
        val getLocation = LatLng(25.8000797854715, -80.2793641534071)
        val cameraPosition = CameraPosition.Builder().target(getLocation).zoom(11f).build()

        val options = GoogleMapOptions().camera(cameraPosition).mapType(GoogleMap.MAP_TYPE_NORMAL)
        val mapFragment = TouchableMapFragment.newInstance(options)
        supportFragmentManager.beginTransaction().add(R.id.mapFragment, mapFragment).commit()

        mapFragment.getMapAsync { google ->
            mapView = google
            // Enable zoom controls
            mapView.uiSettings.isZoomControlsEnabled = true
            mapView.uiSettings.isZoomGesturesEnabled = true

            // Add the custom tile layer
            val customTileProvider = CustomTileLayer(this)
            mapView.addTileOverlay(TileOverlayOptions().tileProvider(customTileProvider))

        }
    }

    //  private var aircraftList1: MutableList<Ac> = mutableListOf()
    // private val aircraftList2: MutableList<Ac> = mutableListOf()
    private var isFirstTime: Boolean = true

    //get all saved route plan details data..........
    private fun subscribeObservers() {
        //simulation mode state get single or multiple routes
        viewModel.globalAirPlanDataState.observe(this) { globeLegState ->
            if (globeLegState.fetchGlobalRoute != null && globeLegState.fetchGlobalRoute.data.isNotEmpty()) {
                if (globeLegState.fetchGlobalRoute.data.isNotEmpty()) {
                    GlobalScope.launch {
                        withContext(Dispatchers.Main) {
                            legLatLngGlobalRouteCopy.clear()
                            legLatLngGlobalRouteCopy.addAll(globeLegState.fetchGlobalRoute.data)
                            drawLegOnMapGlobalRoutes(globeLegState.fetchGlobalRoute.data.toMutableList()) // Draw the new data on the map
                        }
                    }
                }
            }
        }
    }

    private val veryNearThreshold = 500.0 // e.g., 1 km
    private val safeZoneThreshold = 1000.0 // e.g., 5 km
    private val verySafeZoneThreshold = 5000.0 // e.g., 10 km

    private val altitudeThreshold = 100 // e.g., 100 meters


    private fun moveMarker(
        marker: Marker,
        newPosition: LatLng,
        newBearing: Float,
        groundSpeed: Double
    ) {
        val markerAnimator = ValueAnimator.ofObject(LatLngEvaluator(), marker.position, newPosition)

        // Calculate the animation duration based on the ground speed
        val distance = FloatArray(1)
        Location.distanceBetween(
            marker.position.latitude, marker.position.longitude,
            newPosition.latitude, newPosition.longitude, distance
        )
        val animationDuration = (distance[0] / groundSpeed * 5).toLong() // Duration in milliseconds
        Log.d("TAG", "fetchAircraftDetails====== $animationDuration")
        markerAnimator.duration = animationDuration
        markerAnimator.addUpdateListener { animation ->
            marker.position = animation.animatedValue as LatLng
            marker.rotation = newBearing
        }
        markerAnimator.start()
        Log.d("TAG", "drawActiveAirspaces======== ${newBearing}  $groundSpeed  $animationDuration")
    }

    private class LatLngEvaluator : TypeEvaluator<LatLng> {
        override fun evaluate(fraction: Float, startValue: LatLng, endValue: LatLng): LatLng {
            val lat = startValue.latitude + (endValue.latitude - startValue.latitude) * fraction
            val lng = startValue.longitude + (endValue.longitude - startValue.longitude) * fraction
            return LatLng(lat, lng)
        }
    }

    private var createNewFlight = false
    private var index = 0

    fun getColorForIndex(status: Boolean): Int {
        return when (status) {
            true -> Color.GREEN
            false -> Color.BLUE
            else -> Color.GRAY
        }
    }


    fun getColor(status: String): Int {
        return when (status) {
            "RED" -> Color.RED
            "YELLOW" -> Color.YELLOW
            else -> Color.GRAY
        }
    }


    //design globe path and simulation code data fetched from server
   /* private fun drawLegOnMapGlobalRoutes(legLatLngGlobalRouteCopy: MutableList<RouteList>) {
        if (::mapView.isInitialized) {
            legLatLngGlobalRouteCopy.forEachIndexed { _, mapPolylineModal ->
                index++
                createNewFlight = true

                mapPolylineModal.legs.forEach { leg ->
                    Log.d("TAG", "drawLegOnMapGlobalRoutes 111 ====${mapPolylineModal.id}")

                    // Create a list of geopoints for the current leg
                    val routeGeopoints = mutableListOf<LatLng>()
                    routeGeopoints.add(LatLng(leg.startLatitude, leg.startLongitude))
                    routeGeopoints.add(LatLng(leg.endLatitude, leg.endLongitude))

                    // Draw the main route in default color (e.g., green)
                    val polylineOptions = PolylineOptions()
                        .clickable(true)
                        .addAll(routeGeopoints)
                        .color(Color.GREEN)
                        .width(6f)
                        .zIndex(100f)
                        .geodesic(true)
                    val polyline = mapView.addPolyline(polylineOptions)
                    polylineMap[polyline] = mapPolylineModal.id.toInt()

                    // Only process the simulation route for collision overlays
                    if (mapPolylineModal.isSimulationRoute) {
                        mapPolylineModal.legs.forEach { leg ->
                            // Process each collision point to overlay collision segments
                            leg.collisionGeopoints.forEach { collisionObject ->
                                val collisionPoint = LatLng(collisionObject.latitute, collisionObject.longitute)
                                val color = getColor(collisionObject.color) // Use the color provided by the API

                                // Directly draw a short red polyline at each collision point
                                // This assumes you want a small segment around each collision point to be colored
                                val collisionSegment = mutableListOf<LatLng>()

                                // Find a small segment around the collision point, assuming a predefined length
                                val segmentDistance = 500.00 // Approximate distance for the short red line
                                val previousPoint = moveLatLng(collisionPoint, -segmentDistance, leg.heading)
                                val aheadPoint = moveLatLng(collisionPoint, segmentDistance, leg.heading)

                                collisionSegment.add(previousPoint)
                                collisionSegment.add(collisionPoint)
                                collisionSegment.add(aheadPoint)

                                // Create a polyline for the collision area
                                val collisionPolylineOptions = PolylineOptions()
                                    .clickable(true)
                                    .addAll(collisionSegment)
                                    .color(color) // Use the color from the collision object
                                    .width(12f)
                                    .zIndex(101f) // Higher z-index to overlay on main route

                                mapView.addPolyline(collisionPolylineOptions)

                                Log.d("TAG", "Collision detected at $collisionPoint with color $color")
                            }
                        }
                    }
                }

                // Move marker along the route if needed
                if (mapPolylineModal.legs.isNotEmpty()) {
                    lifecycleScope.launch {
                        moveMarkerAlongRoute(
                            mapPolylineModal.legs,
                            mapPolylineModal.isSimulationRoute,
                            mapPolylineModal.id
                        )
                    }
                }
            }
        }
    }*/


    private fun drawLegOnMapGlobalRoutes(legLatLngGlobalRouteCopy: MutableList<RouteList>) {
        if (::mapView.isInitialized) {
            // Initialize polylines according to checkbox state

            if (binding?.resetButton?.isChecked!!) {
                clearAllPolylines()
            } else {
                drawAllPolylines()
            }

            // Handle marker movement
            legLatLngGlobalRouteCopy.forEach { mapPolylineModal ->
                if (mapPolylineModal.legs.isNotEmpty()) {
                    lifecycleScope.launch {
                        moveMarkerAlongRoute(
                            mapPolylineModal.legs,
                            mapPolylineModal.isSimulationRoute,
                            mapPolylineModal.id
                        )
                    }
                }
            }
        }
    }

    private fun clearAllPolylines() {
        // Remove all drawn polylines from the map
        drawnPolylines.forEach { it.remove() }
        drawnPolylines.clear()
    }

    fun moveLatLng(point: LatLng, distance: Double, bearing: Double): LatLng {
        val R = 6371e3 // Earth radius in meters
        val brng = Math.toRadians(bearing) // Convert bearing to radians

        val lat1 = Math.toRadians(point.latitude)
        val lon1 = Math.toRadians(point.longitude)

        val lat2 = Math.asin(
            Math.sin(lat1) * Math.cos(distance / R) +
                    Math.cos(lat1) * Math.sin(distance / R) * Math.cos(brng)
        )

        val lon2 = lon1 + Math.atan2(
            Math.sin(brng) * Math.sin(distance / R) * Math.cos(lat1),
            Math.cos(distance / R) - Math.sin(lat1) * Math.sin(lat2)
        )

        return LatLng(Math.toDegrees(lat2), Math.toDegrees(lon2))
    }


    private fun moveMarkerAlongRoute(
        legs: List<GlobeRouteLeg>,
        simulationRoute: Boolean,
        routeId: Long
    ) {
        if (::mapView.isInitialized) {
            val initialPosition = LatLng(legs.first().startLatitude, legs.first().startLongitude)
            val marker = mapView.addMarker(
                MarkerOptions().position(initialPosition)
                    .icon(getCustomMarkerBitmapDescriptor(true))
            )
            marker?.let {
                markerRouteMap[it] = routeId // Associate the marker with the route ID
            }
            // Set the initial camera position
            setCameraPosition(initialPosition)

            GlobalScope.launch(Dispatchers.Main) {
                for (i in legs.indices) {
                    val leg = legs[i]

                    val startPosition = LatLng(leg.startLatitude, leg.startLongitude)
                    val endPosition = LatLng(leg.endLatitude, leg.endLongitude)

                    // Calculate the distance and time required for the leg
                    val distance = calculateDistance(startPosition, endPosition)
                    val speedInMetersPerSecond = convertKnotsToMetersPerSecond(leg.groundSpeed)
                    val duration =
                        (distance / speedInMetersPerSecond).toLong() * 1000 // Convert to milliseconds

                    // Calculate the bearing
                    val bearing = calculateBearing(startPosition, endPosition)

                    // Move the marker along the leg
                    moveMarker(marker, startPosition, endPosition, duration, bearing).join()
                }
            }
        }
    }

    private fun calculateDistance(startPosition: LatLng, endPosition: LatLng): Double {
        val results = FloatArray(1)
        Location.distanceBetween(
            startPosition.latitude, startPosition.longitude,
            endPosition.latitude, endPosition.longitude, results
        )
        return results[0].toDouble()
    }

    private fun calculateBearing(startPosition: LatLng, endPosition: LatLng): Float {
        val lat1 = Math.toRadians(startPosition.latitude)
        val lon1 = Math.toRadians(startPosition.longitude)
        val lat2 = Math.toRadians(endPosition.latitude)
        val lon2 = Math.toRadians(endPosition.longitude)

        val dLon = lon2 - lon1
        val y = Math.sin(dLon) * Math.cos(lat2)
        val x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon)
        val bearing = Math.toDegrees(Math.atan2(y, x))

        return (bearing + 360).toFloat() % 360
    }

    private fun setCameraPosition(latLng: LatLng) {
        val getLocation = LatLng(25.761681, -80.191788)
        val cameraPosition = CameraPosition.Builder()
            .target(getLocation)
            .zoom(15f) // Adjust the zoom level as needed
            .build()
        mapView.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun convertKnotsToMetersPerSecond(knots: Double): Double {
        return knots * 0.514444
    }

    private suspend fun moveMarker(
        marker: Marker?,
        startPosition: LatLng,
        endPosition: LatLng,
        duration: Long,
        bearing: Float
    ) = coroutineScope {
        val startTime = SystemClock.uptimeMillis()
        launch(Dispatchers.Main) {
            while (true) {
                val elapsedTime = SystemClock.uptimeMillis() - startTime
                val t = elapsedTime.toFloat() / duration
                if (t > 1) break

                val lat = t * endPosition.latitude + (1 - t) * startPosition.latitude
                val lng = t * endPosition.longitude + (1 - t) * startPosition.longitude
                val newPosition = LatLng(lat, lng)

                marker?.position = newPosition
                marker?.rotation = bearing
                delay(16) // Refresh rate of 60 frames per second
            }

            marker?.position = endPosition
            marker?.rotation = bearing
        }
    }


    fun pointsFromCollisionPoint(collision: LatLng, distanceFeet: Double): Pair<LatLng, LatLng> {
        val distanceMeters = distanceFeet * 0.3048 // Convert feet to meters

        // Adjust the bearings as needed for your specific use case
        val bearingForward = 10.0 // Bearing in degrees (north)
        val bearingBackward = 193.0 // Bearing in degrees (south)

        val forwardPoint = coordinateFrom(collision, distanceMeters, bearingForward)
        val backwardPoint = coordinateFrom(collision, distanceMeters, bearingBackward)

        return Pair(forwardPoint, backwardPoint)
    }

    fun coordinateFrom(start: LatLng, distanceMeters: Double, bearing: Double): LatLng {
        val radiusEarth = 6371000.0 // Earth's radius in meters

        val bearingRad = Math.toRadians(bearing) // Convert bearing to radians
        val latRad = Math.toRadians(start.latitude) // Convert latitude to radians
        val lonRad = Math.toRadians(start.longitude) // Convert longitude to radians

        val newLatRad = asin(
            sin(latRad) * cos(distanceMeters / radiusEarth) +
                    cos(latRad) * sin(distanceMeters / radiusEarth) * cos(bearingRad)
        )

        val newLonRad = lonRad + atan2(
            sin(bearingRad) * sin(distanceMeters / radiusEarth) * cos(latRad),
            cos(distanceMeters / radiusEarth) - sin(latRad) * sin(newLatRad)
        )

        val newLat = Math.toDegrees(newLatRad) // Convert back to degrees
        val newLon = Math.toDegrees(newLonRad) // Convert back to degrees

        return LatLng(newLat, newLon)
    }

    fun addMarkerCollisionPoint(
        mapView: GoogleMap,
        coordinate: LatLng,
        title: String? = null,
        snippet: String? = null,
        icon: BitmapDescriptor? = null
    ) {
        val marker = mapView.addMarker(
            MarkerOptions().position(coordinate)
                .title(title)
                .snippet(snippet)
                .icon(icon)
        )
        mapView.setMinZoomPreference(0f)
        mapView.setMaxZoomPreference(20f)

        val (forward, backward) = pointsFromCollisionPoint(
            collision = coordinate,
            distanceFeet = 720.0
        )

        // Create a polyline for the backward direction
        val backwardPath = PolylineOptions()
            .add(coordinate)
            .add(backward)
            .color(Color.YELLOW) // Change color as needed
            .width(30f)
        mapView.addPolyline(backwardPath)

        // Create a polyline for the forward direction
        val forwardPath = PolylineOptions()
            .add(coordinate)
            .add(forward)
            .color(Color.GREEN) // Change color as needed
            .width(30f)
        mapView.addPolyline(forwardPath)
    }


    private fun getCustomMarkerBitmapDescriptor(status: Boolean): BitmapDescriptor {
        var customMarkerView: View? = null
        if (status) {
            customMarkerView = (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                .inflate(R.layout.custom_marker, null)
        } else {
            Log.d("TAG", "getCustomMarkerBitmapDescriptor: eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee")
            customMarkerView = (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                .inflate(R.layout.custom_yellow, null)
        }
        customMarkerView?.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView?.layout(
            0,
            0,
            customMarkerView.measuredWidth,
            customMarkerView.measuredHeight
        )

        val bitmap = Bitmap.createBitmap(
            customMarkerView.measuredWidth,
            customMarkerView.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        customMarkerView.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun calculatePointFromDistance(latLng: LatLng, distance: Double, bearing: Double): LatLng {
        return SphericalUtil.computeOffset(latLng, distance, bearing)
    }


    fun findPointAtDistance(geopoints: List<LatLng>, origin: LatLng, distance: Double, isAhead: Boolean): LatLng? {
        var totalDistance = 0.0

        if (isAhead) {
            for (i in geopoints.indices) {
                val startPoint = geopoints[i]
                if (i + 1 < geopoints.size) {
                    val endPoint = geopoints[i + 1]
                    totalDistance += calculateDistanceBetween(startPoint, endPoint)
                    if (totalDistance >= distance) {
                        return endPoint
                    }
                }
            }
        } else {
            for (i in geopoints.size - 1 downTo 1) {
                val startPoint = geopoints[i]
                val endPoint = geopoints[i - 1]
                totalDistance += calculateDistanceBetween(startPoint, endPoint)
                if (totalDistance >= distance) {
                    return endPoint
                }
            }
        }
        return null
    }
    fun calculateDistanceBetween(start: LatLng, end: LatLng): Double {
        val results = FloatArray(1)
        Location.distanceBetween(
            start.latitude, start.longitude,
            end.latitude, end.longitude,
            results
        )
        return results[0].toDouble()
    }


    // Keep track of all polylines
    private val drawnPolylines = mutableListOf<Polyline>()

    private fun drawAllPolylines() {
        // Redraw polylines on the map from stored data
        legLatLngGlobalRouteCopy.forEach { mapPolylineModal ->
            mapPolylineModal.legs.forEach { leg ->
                val routeGeopoints = mutableListOf<LatLng>()
                routeGeopoints.add(LatLng(leg.startLatitude, leg.startLongitude))
                routeGeopoints.add(LatLng(leg.endLatitude, leg.endLongitude))

                val polylineOptions = PolylineOptions()
                    .clickable(true)
                    .addAll(routeGeopoints)
                    .color(Color.GREEN)
                    .width(6f)
                    .zIndex(100f)
                    .geodesic(true)
                val polyline = mapView.addPolyline(polylineOptions)
                drawnPolylines.add(polyline) // Store reference to the drawn polyline

                if (mapPolylineModal.isSimulationRoute) {
                    drawCollisionSegments(leg)
                }
            }
        }
    }

    private fun drawCollisionSegments(leg: GlobeRouteLeg) {
        leg.collisionGeopoints.forEach { collisionObject ->
            val collisionPoint = LatLng(collisionObject.latitute, collisionObject.longitute)
            val color = getColor(collisionObject.color)

            val collisionSegment = mutableListOf<LatLng>()
            val segmentDistance = 500.00
            val previousPoint = moveLatLng(collisionPoint, -segmentDistance, leg.heading)
            val aheadPoint = moveLatLng(collisionPoint, segmentDistance, leg.heading)

            collisionSegment.add(previousPoint)
            collisionSegment.add(collisionPoint)
            collisionSegment.add(aheadPoint)

            val collisionPolylineOptions = PolylineOptions()
                .clickable(true)
                .addAll(collisionSegment)
                .color(color)
                .width(12f)
                .zIndex(101f)

            val collisionPolyline = mapView.addPolyline(collisionPolylineOptions)
            drawnPolylines.add(collisionPolyline) // Store reference to the collision polyline
        }
    }
}
