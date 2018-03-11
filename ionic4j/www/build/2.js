webpackJsonp([2],{

/***/ 725:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "TabsPageModule", function() { return TabsPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__ngx_translate_core__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_ionic_angular__ = __webpack_require__(63);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__tabs__ = __webpack_require__(734);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var TabsPageModule = (function () {
    function TabsPageModule() {
    }
    TabsPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["J" /* NgModule */])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_3__tabs__["a" /* TabsPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_2_ionic_angular__["f" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_3__tabs__["a" /* TabsPage */]),
                __WEBPACK_IMPORTED_MODULE_1__ngx_translate_core__["b" /* TranslateModule */].forChild()
            ],
            exports: [
                __WEBPACK_IMPORTED_MODULE_3__tabs__["a" /* TabsPage */]
            ]
        })
    ], TabsPageModule);
    return TabsPageModule;
}());

//# sourceMappingURL=tabs.module.js.map

/***/ }),

/***/ 734:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return TabsPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};

var TabsPage = (function () {
    function TabsPage() {
        // tab1Root: any = Tab1Root;
        // tab2Root: any = Tab2Root;
        // tab3Root: any = Tab3Root;
        // tab4Root: any = Tab4Root;
        //
        // tab1Title = " ";
        // tab2Title = " ";
        // tab3Title = " ";
        // tab4Title = " ";
        //
        // constructor(public navCtrl: NavController, public translateService: TranslateService) {
        //   translateService.get(['TAB1_TITLE', 'TAB2_TITLE', 'TAB3_TITLE', 'TAB4_TITLE']).subscribe(values => {
        //     this.tab1Title = values['TAB1_TITLE'];
        //     this.tab2Title = values['TAB2_TITLE'];
        //     this.tab3Title = values['TAB3_TITLE'];
        //     this.tab4Title = values['TAB4_TITLE'];
        //   });
        // }
        this.tab1Root = 'HomePage';
        this.tab2Root = 'EntityPage';
        this.tab3Root = 'SettingsPage';
        this.tab4Root = 'SettingsPage';
    }
    TabsPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["n" /* Component */])({
            selector: 'page-tabs',template:/*ion-inline-start:"C:\Users\Lambert\Documents\GitHub\EasyFlight\ionic4j\src\pages\tabs\tabs.html"*/'<!--<ion-tabs>-->\n\n  <!--<ion-tab [root]="tab1Root" [tabTitle]="tab1Title" tabIcon="home"></ion-tab>-->\n\n  <!--<ion-tab [root]="tab2Root" [tabTitle]="tab2Title" tabIcon="menu"></ion-tab>-->\n\n  <!--<ion-tab [root]="tab3Root" [tabTitle]="tab3Title" tabIcon="cog"></ion-tab>-->\n\n  <!--<ion-tab [root]="tab4Root" [tabTitle]="tab4Title" tabIcon="cog"></ion-tab>-->\n\n<!--</ion-tabs>-->\n\n\n\n<ion-tabs tabs-only>\n\n  <ion-tab tabTitle="Résumé" tabIcon="ios-paper" [root]="tab1Root"></ion-tab>\n\n  <ion-tab tabTitle="Météo" tabIcon="partly-sunny" [root]="tab2Root"></ion-tab>\n\n  <ion-tab tabTitle="Notams" tabIcon="warning" [root]="tab3Root"></ion-tab>\n\n  <ion-tab tabTitle="Masse & centrage" tabIcon="calculator" [root]="tab4Root"></ion-tab>\n\n</ion-tabs>'/*ion-inline-end:"C:\Users\Lambert\Documents\GitHub\EasyFlight\ionic4j\src\pages\tabs\tabs.html"*/
        }),
        __metadata("design:paramtypes", [])
    ], TabsPage);
    return TabsPage;
}());

//# sourceMappingURL=tabs.js.map

/***/ })

});
//# sourceMappingURL=2.js.map