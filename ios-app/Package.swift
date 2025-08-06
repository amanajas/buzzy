// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "FrequencyApp",
    platforms: [
        .iOS(.v16)
    ],
    products: [
        .library(
            name: "FrequencyApp",
            targets: ["FrequencyApp"]),
    ],
    dependencies: [
        .package(url: "https://github.com/firebase/firebase-ios-sdk.git", from: "10.0.0")
    ],
    targets: [
        .target(
            name: "FrequencyApp",
            dependencies: [
                .product(name: "FirebaseAnalytics", package: "firebase-ios-sdk"),
                .product(name: "FirebaseAnalyticsSwift", package: "firebase-ios-sdk")
            ]),
        .testTarget(
            name: "FrequencyAppTests",
            dependencies: ["FrequencyApp"]),
    ]
)