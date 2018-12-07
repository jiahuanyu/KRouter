package me.jiahuan.android.krouter.compiler

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import me.jiahuan.android.krouter.annotation.Consts
import me.jiahuan.android.krouter.annotation.Route
import me.jiahuan.android.krouter.annotation.RouteMeta
import me.jiahuan.android.krouter.annotation.RouteType
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types


@AutoService(Processor::class)
@SupportedOptions(Consts.KAPT_KOTLIN_GENERATED_OPTION_NAME)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class RouteProcessor : AbstractProcessor() {

    private lateinit var mLogger: Logger
    private lateinit var mElementUtil: Elements
    private lateinit var mTypeUtil: Types
    private var mModuleName = ""

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        mLogger = Logger(processingEnv.messager)
        mElementUtil = processingEnv.elementUtils
        mTypeUtil = processingEnv.typeUtils
        mLogger.info("init")
        val options = processingEnv.options
        if (options.isNotEmpty()) {
            val name = options["moduleName"] ?: ""
            mModuleName = name.replace("[^0-9a-zA-Z_]+".toRegex(), "")
        }
    }

    override fun process(set: MutableSet<out TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        val routeElements = roundEnvironment.getElementsAnnotatedWith(Route::class.java)
        if (routeElements != null && routeElements.size > 0) {

            val mapTypeOfRouteLoader = ParameterizedTypeName.get(
                ClassName("kotlin.collections", "MutableMap"),
                String::class.asClassName(),
                RouteMeta::class.asClassName()
            )

            val routeLoaderFunSpecBuild = FunSpec.builder(Consts.METHOD_LOAD)
                .addParameter("map", mapTypeOfRouteLoader)
                .addModifiers(KModifier.OVERRIDE)

            val tmActivity = mElementUtil.getTypeElement(RouteType.ACTIVITY.className).asType()
            val tmService = mElementUtil.getTypeElement(RouteType.SERVICE.className).asType()


            routeElements.forEach {

                val routeAnn = it.getAnnotation(Route::class.java)

                val routeType = when {
                    mTypeUtil.isSubtype(it.asType(), tmActivity) -> {
                        mLogger.info("found activity ")
                        RouteType.ACTIVITY
                    }
                    mTypeUtil.isSubtype(it.asType(), tmService) -> {
                        mLogger.info("found service ")
                        RouteType.SERVICE
                    }
                    else -> {
                        RouteType.UNKNOWN
                    }
                }

                routeLoaderFunSpecBuild.addStatement(
                    "map[%S] = %T(%T.%L, %S, %T::class.java)",
                    routeAnn.path,
                    RouteMeta::class,
                    RouteType::class,
                    routeType,
                    routeAnn.path,
                    it.asType()
                )
            }

            val typeIRouteLoader = TypeSpec.classBuilder("${Consts.AUTO_GENERATOR_CLASS_PREFIX}$mModuleName")
                .addSuperinterface(ClassName.bestGuess(Consts.IROUTELOADER_CLASS))
                .addFunction(routeLoaderFunSpecBuild.build())
                .build()

            val kotlinFile = FileSpec.builder(Consts.PACKAGE, "${Consts.AUTO_GENERATOR_CLASS_PREFIX}$mModuleName")
                .addType(typeIRouteLoader)
                .build()
            val kaptKotlinGeneratedDir = processingEnv.options[Consts.KAPT_KOTLIN_GENERATED_OPTION_NAME]
            val outputFile = File(kaptKotlinGeneratedDir).apply {
                mkdirs()
            }
            kotlinFile.writeTo(outputFile)
        }
        return true
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val set = LinkedHashSet<String>()
        set.add(Route::class.java.canonicalName)
        return set
    }
}