private fun drawAllPolylines() {
  clearAllPolylines()

  // Draw all routes with green color initially
  legLatLngGlobalRouteCopy.forEach { mapPolylineModal ->
  mapPolylineModal.legs.forEach { leg ->
  val routeGeopoints = listOf(
  LatLng(leg.startLatitude, leg.startLongitude),
  LatLng(leg.endLatitude, leg.endLongitude)
  )

  val polylineOptions = PolylineOptions()
      .clickable(true)
      .addAll(routeGeopoints)
      .color(Color.GREEN)
      .width(6f)
      .zIndex(100f)
      .geodesic(true)

  val polyline = mapView.addPolyline(polylineOptions)
  drawnPolylines.add(polyline) // Store reference to the drawn polyline
  }
}

  // After drawing all routes, apply visibility filters based on checkboxes
  applyPolylineVisibility()
}

private fun applyPolylineVisibility() {
  // Identify the simulation route
  val myRoute = legLatLngGlobalRouteCopy.find { it.isSimulationRoute }

  // Clear previous classifications
  collisionPolylines.clear()
  nearbyPolylines.clear()
  safeDistancePolylines.clear()

  if (myRoute != null) {
    // Compare all other routes to the simulation route
    legLatLngGlobalRouteCopy.forEach { mapPolylineModal ->
  if (mapPolylineModal != myRoute) {
  mapPolylineModal.legs.forEach { leg ->
  myRoute.legs.forEach { myLeg ->
  val distance = calculateDistanceBetween(
  LatLng(myLeg.startLatitude, myLeg.startLongitude),
  LatLng(leg.startLatitude, leg.startLongitude)
  )

  when {
  distance <= 1000 -> collisionPolylines.add(getPolylineForLeg(leg))
  distance <= 2000 -> nearbyPolylines.add(getPolylineForLeg(leg))
  distance <= 5000 -> safeDistancePolylines.add(getPolylineForLeg(leg))
  }
  }
  }
  }
  }
  }

  // Show or hide polylines based on checkbox states
  collisionPolylines.forEach { polyline ->
  polyline.isVisible = binding?.collision?.isChecked == true
  }

  nearbyPolylines.forEach { polyline ->
  polyline.isVisible = binding?.nearBy?.isChecked == true
  }

  safeDistancePolylines.forEach { polyline ->
  polyline.isVisible = binding?.safe?.isChecked == true
  }

  // Ensure planned routes are shown if that checkbox is checked
  drawnPolylines.forEach { polyline ->
  polyline.isVisible = binding?.showPlanned?.isChecked == true
  }
}

private fun getPolylineForLeg(leg: Leg): Polyline {
return drawnPolylines.find { polyline ->
val points = polyline.points
points.first().latitude == leg.startLatitude && points.first().longitude == leg.startLongitude &&
points.last().latitude == leg.endLatitude && points.last().longitude == leg.endLongitude
} ?: throw IllegalStateException("Polyline for leg not found")
}


