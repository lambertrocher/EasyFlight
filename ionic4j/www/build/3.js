webpackJsonp([3],{

/***/ 723:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "SignupPageModule", function() { return SignupPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__ngx_translate_core__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_ionic_angular__ = __webpack_require__(63);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__signup__ = __webpack_require__(731);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var SignupPageModule = (function () {
    function SignupPageModule() {
    }
    SignupPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["J" /* NgModule */])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_3__signup__["a" /* SignupPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_2_ionic_angular__["f" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_3__signup__["a" /* SignupPage */]),
                __WEBPACK_IMPORTED_MODULE_1__ngx_translate_core__["b" /* TranslateModule */].forChild()
            ],
            exports: [
                __WEBPACK_IMPORTED_MODULE_3__signup__["a" /* SignupPage */]
            ]
        })
    ], SignupPageModule);
    return SignupPageModule;
}());

//# sourceMappingURL=signup.module.js.map

/***/ }),

/***/ 731:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return SignupPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__ngx_translate_core__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_ionic_angular__ = __webpack_require__(63);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__providers_providers__ = __webpack_require__(168);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__pages__ = __webpack_require__(381);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};





var SignupPage = (function () {
    function SignupPage(navCtrl, user, toastCtrl, translateService) {
        var _this = this;
        this.navCtrl = navCtrl;
        this.user = user;
        this.toastCtrl = toastCtrl;
        this.translateService = translateService;
        // The account fields for the login form.
        // If you're using the username field with or without email, make
        // sure to add it to the type
        this.account = {
            login: '',
            email: '',
            firstName: '',
            lastName: '',
            password: '',
            langKey: 'en'
        };
        this.translateService.get('SIGNUP_ERROR').subscribe(function (value) {
            _this.signupErrorString = value;
        });
    }
    SignupPage.prototype.doSignup = function () {
        var _this = this;
        // set login to same as email
        this.account.login = this.account.email;
        // Attempt to login in through our User service
        this.user.signup(this.account).subscribe(function (resp) {
            _this.navCtrl.push(__WEBPACK_IMPORTED_MODULE_4__pages__["b" /* MainPage */]);
        }, function (err) {
            console.log('error in signup', err);
            // Unable to sign up
            var toast = _this.toastCtrl.create({
                message: _this.signupErrorString,
                duration: 3000,
                position: 'top'
            });
            toast.present();
        });
    };
    SignupPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["n" /* Component */])({
            selector: 'page-signup',template:/*ion-inline-start:"C:\Users\Lambert\Documents\GitHub\EasyFlight\ionic4j\src\pages\signup\signup.html"*/'<ion-header>\n\n\n\n  <ion-navbar>\n\n    <ion-title>{{ \'SIGNUP_TITLE\' | translate }}</ion-title>\n\n  </ion-navbar>\n\n\n\n</ion-header>\n\n\n\n\n\n<ion-content>\n\n  <form (submit)="doSignup()">\n\n    <ion-list>\n\n\n\n      <ion-item>\n\n        <ion-label fixed>{{ \'NAME\' | translate }}</ion-label>\n\n        <ion-input type="text" [(ngModel)]="account.name" name="name"></ion-input>\n\n      </ion-item>\n\n\n\n      <ion-item>\n\n        <ion-label fixed>{{ \'EMAIL\' | translate }}</ion-label>\n\n        <ion-input type="email" [(ngModel)]="account.email" name="email"></ion-input>\n\n      </ion-item>\n\n\n\n      <!--\n\n      Want to add a Username? Here you go:\n\n\n\n      <ion-item>\n\n        <ion-label floating>Username</ion-label>\n\n        <ion-input type="text" [(ngModel)]="account.username" name="username"></ion-input>\n\n      </ion-item>\n\n      -->\n\n\n\n      <ion-item>\n\n        <ion-label fixed>{{ \'PASSWORD\' | translate }}</ion-label>\n\n        <ion-input type="password" [(ngModel)]="account.password" name="password"></ion-input>\n\n      </ion-item>\n\n\n\n      <div padding>\n\n        <button ion-button color="primary" block>{{ \'SIGNUP_BUTTON\' | translate }}</button>\n\n      </div>\n\n\n\n    </ion-list>\n\n  </form>\n\n</ion-content>'/*ion-inline-end:"C:\Users\Lambert\Documents\GitHub\EasyFlight\ionic4j\src\pages\signup\signup.html"*/
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_2_ionic_angular__["i" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_3__providers_providers__["c" /* User */],
            __WEBPACK_IMPORTED_MODULE_2_ionic_angular__["l" /* ToastController */],
            __WEBPACK_IMPORTED_MODULE_1__ngx_translate_core__["c" /* TranslateService */]])
    ], SignupPage);
    return SignupPage;
}());

//# sourceMappingURL=signup.js.map

/***/ })

});
//# sourceMappingURL=3.js.map