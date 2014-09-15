package com.bloomhealth.tinker

import com.google.inject.AbstractModule
import com.google.inject.Scopes

class TinkerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TinkerService).in(Scopes.SINGLETON)
    }
}
