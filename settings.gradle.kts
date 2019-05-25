pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}
rootProject.name = "everywear-model-server"

/**
 * ew-core: 도메인 및 공통 사용 컴포넌트
 * ew-synthesis: 딥러닝 모델 인퍼런스 및 결과 업로드
 * ew-fitting: 피팅 API 및 푸시 서버
 */
include("ew-core", "ew-synthesis", "ew-fitting")