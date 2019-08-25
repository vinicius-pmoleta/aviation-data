package com.aviationdata.features.search

import com.aviationdata.core.Aircraft
import com.aviationdata.core.Identification
import com.aviationdata.core.Information
import com.aviationdata.core.Operation

fun RawSearchResult.toAircraft() = Aircraft(
    identification = Identification(
        icao24 = icao24,
        registration = registration
    ),
    operation = Operation(
        operator = operator,
        country = country
    ),
    information = Information(
        model = model
    )
)