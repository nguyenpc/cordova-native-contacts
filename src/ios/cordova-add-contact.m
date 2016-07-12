/********* cordova-add-contact.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>
#import "cordova-add-contact.h"

@implementation CordovaAddContact

- (void)addContact:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* echo = [command.arguments objectAtIndex:0];

    if (echo != nil && [echo length] > 0) {

        CNContact *contact = [[CNContact alloc] init];

        _addContactVC = [CNContactViewController viewControllerForNewContact:contact];
        _addContactVC.delegate = self;
        _addContactVC.allowsEditing = YES;
        _addContactVC.allowsActions = YES;

        _navController = [[UINavigationController alloc] initWithRootViewController:_addContactVC];
        
        // Display the view
        [self.viewController presentViewController:_navController animated:YES completion:^{}];

        //pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:command.callbackId];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

-(void)contactViewController:(CNContactViewController *)viewController didCompleteWithContact:(CNContact *)contact {
    [self.viewController dismissViewControllerAnimated:YES completion:nil];
}



@end