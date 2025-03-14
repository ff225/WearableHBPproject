package it.unibo.alessiociarrocchi.tesiahc.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "ipfs_table", indices = [Index(value = ["cid"], unique = true)])
data class IPFSEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val cid: String
)