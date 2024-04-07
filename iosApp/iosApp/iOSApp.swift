import SwiftUI
import AppCenter
import AppCenterCrashes
import AppCenterAnalytics
//import FirebaseCore
//
//class AppDelegate: NSObject, UIApplicationDelegate {
//  func application(_ application: UIApplication,
//                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
//    FirebaseApp.configure()
//
//    return true
//  }
//}

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        AppCenter.start(withAppSecret: "9de1a658-f1fe-46a3-a0de-97ae0e595cdc", services:[
            Analytics.self,
            Crashes.self,
            Distribute.self
        ])
        return true
        }
        }

@main
struct iOSApp: App {
    //@UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
