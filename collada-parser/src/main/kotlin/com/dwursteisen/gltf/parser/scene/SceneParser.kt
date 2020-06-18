package com.dwursteisen.gltf.parser.scene

import com.adrienben.tools.gltf.models.GltfAsset
import com.dwursteisen.gltf.parser.camera.CameraParser
import com.dwursteisen.gltf.parser.material.MaterialParser
import com.dwursteisen.gltf.parser.model.ModelParser
import com.dwursteisen.minigdx.scene.api.Scene

class SceneParser(private val gltfAsset: GltfAsset) {

    private val cameras = CameraParser(gltfAsset)

    private val models = ModelParser(gltfAsset)

    private val materials = MaterialParser(gltfAsset)

    fun parse(): Scene {
        return Scene(
            perspectiveCameras = cameras.perspectiveCameras(),
            orthographicCameras = cameras.orthographicCameras(),
            models = models.objects(),
            materials = materials.materials()
        )
    }
}