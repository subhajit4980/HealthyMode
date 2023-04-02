package com.example.HealthyMode.Backup

import android.app.backup.BackupAgentHelper
import android.app.backup.SharedPreferencesBackupHelper

// The name of the SharedPreferences file
const val PREFS = "step_count"

// A key to uniquely identify the set of backup data
const val PREFS_BACKUP_KEY = "prefs"

class BackupAgent : BackupAgentHelper() {
    override fun onCreate() {
        // Allocate a helper and add it to the backup agent
        SharedPreferencesBackupHelper(this, PREFS).also {
            addHelper(PREFS_BACKUP_KEY, it)
        }
    }
}