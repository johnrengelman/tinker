package com.bloomhealth.tinker.model

import com.bloomhealth.tinker.model.Diff

class ComparisonResult {

    List<Diff> diffs = []

    boolean isMatch() {
        return diffs.isEmpty()
    }
}
