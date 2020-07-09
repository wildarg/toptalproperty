package com.homesoftwaretools.toptalproperty.repo

import com.homesoftwaretools.toptalproperty.domain.Location
import io.reactivex.Single

interface GeocodeRepo {
    fun decode(address: String, key: String): Single<Location>
}