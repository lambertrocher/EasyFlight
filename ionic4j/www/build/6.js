webpackJsonp([6],{

/***/ 723:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "NotamsPageModule", function() { return NotamsPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(63);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__notams__ = __webpack_require__(735);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



var NotamsPageModule = (function () {
    function NotamsPageModule() {
    }
    NotamsPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["J" /* NgModule */])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__notams__["a" /* NotamsPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["f" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__notams__["a" /* NotamsPage */]),
            ],
            exports: [
                __WEBPACK_IMPORTED_MODULE_2__notams__["a" /* NotamsPage */]
            ]
        })
    ], NotamsPageModule);
    return NotamsPageModule;
}());

//# sourceMappingURL=notams.module.js.map

/***/ }),

/***/ 735:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return NotamsPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(63);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__providers_auth_principal_service__ = __webpack_require__(168);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__pages__ = __webpack_require__(381);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__providers_login_login_service__ = __webpack_require__(167);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};





/**
 * Generated class for the NotamsPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var NotamsPage = (function () {
    function NotamsPage(navCtrl, principal, app, loginService) {
        this.navCtrl = navCtrl;
        this.principal = principal;
        this.app = app;
        this.loginService = loginService;
    }
    NotamsPage.prototype.ngOnInit = function () {
        var _this = this;
        this.principal.identity().then(function (account) {
            if (account === null) {
                _this.app.getRootNavs()[0].setRoot(__WEBPACK_IMPORTED_MODULE_3__pages__["a" /* FirstRunPage */]);
            }
            else {
                _this.account = account;
            }
        });
    };
    NotamsPage.prototype.isAuthenticated = function () {
        return this.principal.isAuthenticated();
    };
    NotamsPage.prototype.logout = function () {
        this.loginService.logout();
        this.app.getRootNavs()[0].setRoot(__WEBPACK_IMPORTED_MODULE_3__pages__["a" /* FirstRunPage */]);
    };
    NotamsPage.prototype.notams = function () {
        this.navCtrl.push('NotamsPage');
    };
    NotamsPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["n" /* Component */])({
            selector: 'page-notams',template:/*ion-inline-start:"C:\Users\Lambert\Documents\GitHub\EasyFlight\ionic4j\src\pages\notams\notams.html"*/'<!--\n  Generated template for the NotamsPage page.\n\n  See http://ionicframework.com/docs/components/#navigation for more info on\n  Ionic pages and navigation.\n-->\n<ion-header>\n\n  <ion-navbar>\n    <ion-title>Notams</ion-title>\n  </ion-navbar>\n\n</ion-header>\n\n\n<ion-content padding>\n\n</ion-content>\n'/*ion-inline-end:"C:\Users\Lambert\Documents\GitHub\EasyFlight\ionic4j\src\pages\notams\notams.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["i" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_2__providers_auth_principal_service__["a" /* Principal */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["a" /* App */],
            __WEBPACK_IMPORTED_MODULE_4__providers_login_login_service__["a" /* LoginService */]])
    ], NotamsPage);
    return NotamsPage;
}());

//# sourceMappingURL=notams.js.map

/***/ })

});
//# sourceMappingURL=6.js.map