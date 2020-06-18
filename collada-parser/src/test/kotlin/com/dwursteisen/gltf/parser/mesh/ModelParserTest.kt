package com.dwursteisen.gltf.parser.mesh

import com.curiouscreature.kotlin.math.Float3
import com.curiouscreature.kotlin.math.Mat4
import com.curiouscreature.kotlin.math.translation
import com.dwursteisen.gltf.parser.model.ModelParser
import com.dwursteisen.gltf.parser.support.assertMat4Equals
import com.dwursteisen.gltf.parser.support.assertPositionEquals
import com.dwursteisen.gltf.parser.support.gltf
import com.dwursteisen.minigdx.scene.api.model.Position
import com.dwursteisen.minigdx.scene.api.model.UV
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ModelParserTest {

    private val cube by gltf("/mesh/cube_translated.gltf")
    private val plane by gltf("/mesh/plane.gltf")
    private val simpleUv by gltf("/uv/uv.gltf")
    private val multipleUv by gltf("/uv/multiple_materials.gltf")

    @Test
    fun `parse | it parses a translated cube`() {
        val objects = ModelParser(cube).objects()

        assertEquals(1, objects.size)

        val cube = objects.getValue("Cube")
        val transformation = translation(Float3(1f, 3f, -2f))

        assertMat4Equals(transformation, Mat4.fromColumnMajor(*cube.transformation.matrix))
    }

    @Test
    fun `parse | it parses primitives of a cube`() {
        val objects = ModelParser(cube).objects()

        val cube = objects.getValue("Cube").mesh
        assertEquals(1, cube.primitives.size)
        // TODO: should try to be close to 8 instead
        assertEquals(24, cube.primitives.first().vertices.size)
    }

    @Test
    fun `parse | it parses a plane with correct coordinates`() {
        val objects = ModelParser(plane).objects()

        val cube = objects.getValue("Plane").mesh
        assertEquals(1, cube.primitives.size)
        assertEquals(4, cube.primitives.first().vertices.size)

        val (a, b, c, d) = cube.primitives.first().vertices

        assertPositionEquals(Position(0f, 0f, 0f), a.position)
        assertPositionEquals(Position(1f, 0f, 0f), b.position)
        assertPositionEquals(Position(0f, 0f, -2f), c.position)
        assertPositionEquals(Position(0f, 3f, 0f), d.position)
    }

    @Test
    fun `parse | it parses a mesh with no material`() {
        val objects = ModelParser(cube).objects()
        val uvs = objects.flatMap { it.value.mesh.primitives }
            .flatMap { it.vertices }
            .map { it.uv }
            .toSet()

        assertTrue(uvs.contains(UV.INVALID))
        assertEquals(1, uvs.size)
        assertEquals(-1, objects.getValue("Cube").mesh.primitives.first().materialId)
    }

    @Test
    fun `parse | it parses a mesh with one material`() {
        val objects = ModelParser(simpleUv).objects()
        val uvs = objects.flatMap { it.value.mesh.primitives }
            .flatMap { it.vertices }
            .map { it.uv }

        assertTrue(uvs.isNotEmpty())
        assertFalse(uvs.contains(UV.INVALID))
    }

    @Test
    fun `parse | it parses a mesh with more than one material`() {
        val objects = ModelParser(multipleUv).objects()
        val materials = objects.flatMap { it.value.mesh.primitives }
            .map { it.materialId }

        assertEquals(2, materials.size)
    }
}