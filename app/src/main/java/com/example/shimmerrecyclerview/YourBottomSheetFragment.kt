package com.example.shimmerrecyclerview


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class YourBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var tvFlightNumber: TextView
    private lateinit var tvAirline: TextView
    private lateinit var ivAirlineLogo: ImageView
    private lateinit var tvDeparture: TextView
    private lateinit var tvFlightStatus: TextView
    private lateinit var tvArrival: TextView
    private lateinit var tvBarometricAltitude: TextView
    private lateinit var tvGroundSpeed: TextView
    private lateinit var tvAircraft: TextView
    private lateinit var tvRegistration: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        tvFlightNumber = view.findViewById(R.id.tv_flight_number)
        tvAirline = view.findViewById(R.id.tv_airline)
        ivAirlineLogo = view.findViewById(R.id.iv_airline_logo)
        tvDeparture = view.findViewById(R.id.tv_departure)
        tvFlightStatus = view.findViewById(R.id.tv_flight_status)
        tvArrival = view.findViewById(R.id.tv_arrival)
        tvBarometricAltitude = view.findViewById(R.id.tv_barometric_altitude)
        tvGroundSpeed = view.findViewById(R.id.tv_ground_speed)
        tvAircraft = view.findViewById(R.id.tv_aircraft)
        tvRegistration = view.findViewById(R.id.tv_registration)

        // Set the data
        tvFlightNumber.text = "TRA27G"
        tvAirline.text = "Transavia"
        ivAirlineLogo.setImageResource(R.drawable.shimmer_background)
        tvDeparture.text = "GRO\nGIRONA"
        tvFlightStatus.text = "Departed 01:29 ago"
        tvArrival.text = "RTM\nROTTERDAM"
        tvBarometricAltitude.text = "8,150 ft"
        tvGroundSpeed.text = "303 kts"
        tvAircraft.text = "Boeing 737-8K2"
        tvRegistration.text = "PH-HZN"
    }
}
