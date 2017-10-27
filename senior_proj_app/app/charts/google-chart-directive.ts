import {Component, Directive, ElementRef, Input, Output, OnChanges, EventEmitter, HostBinding, HostListener} from '@angular/core';

// import {} from '@types/google.visualization'; //importing the google visualization namespace, but doesn't have all types

declare const google: any;
declare let googleLoaded: boolean;
declare const googleChartsPackagesToLoad: any;
@Directive({
    selector: '[GoogleChart]'
})
export class GoogleChart implements OnChanges {
    public _element: ElementRef;
    @Input('chartType') public chartType: string;
    @Input('chartOptions') public chartOptions: Object;
    @Input('loadingDelay') public loadingDelay = 0;
    @Input('chartData') public chartData: Object;
    @Input() public enableStaticImage: boolean = false;
    @Input() public enableControls: boolean = false;
    @Input() public divIds: string[] = [];
    @Output('itemSelect') public itemSelect: EventEmitter<{ row: number, column: number }> = new EventEmitter();
    @Output('itemDeselect') public itemDeselect: EventEmitter<void> = new EventEmitter<void>();

    private isPngCreated: boolean = false; //allowing chart png image to only be emitted once

    constructor(public element: ElementRef) {
        this._element = this.element.nativeElement;
    }

    ngOnChanges() {
        if (!googleLoaded) {
            googleLoaded = true;
            google.charts.load('current', {'packages': ['corechart', 'controls', 'charteditor']});
        }
        setTimeout(() => this.drawGraph(this.chartOptions, this.chartType, this.chartData, this._element), this.loadingDelay);
    }

    @HostListener('window:resize') onResize(event: Event) {
        this.drawGraph(this.chartOptions, this.chartType, this.chartData, this._element);
    }

    wrapper: any; //google.visualization.ChartWrapper
    customWrapper: any; //google.visualization.ChartWrapper
    chartEditor: any; //google.visualization.ChartEditor
    dashboard: any; //google.visualization.Dashboard

    states: any[] = []; //google.visualization.ControlWrapper.getState()
    
    drawGraph(chartOptions, chartType, chartData, ele) {
        google.charts.setOnLoadCallback(drawChart);
        const self = this;

        function drawChart() {
            //create the chart wrapper with the given chartData and chartOptions
            self.wrapper = new google.visualization.ChartWrapper({                    
                chartType: chartType,
                dataTable: chartData,
                options: chartOptions || {}
            });

            //only create controls if the option is enabled and there is chartData to work with
            if (self.enableControls && self.chartData) {
                //create a dashboard for the control wrappers, passing in the div for the google chart
                self.dashboard = new google.visualization.Dashboard(self._element);  
                
                var controlWrappers: any[] = []; //google.visualization.ControlWrapper                        
                
                //looping through the chartData columns to determine what kind of filter is needed for the ControlWrapper
                for (let i = 0; i < self.chartData["cols"].length; i++) {
                    let filterType: string = '';
                    switch(self.chartData["cols"][i].type) {
                        case "string":
                            filterType = "CategoryFilter";
                            break;
                        case "number":
                            filterType = "NumberRangeFilter";
                            break;
                        default:
                            filterType = '';
                    }
                    
                    //assigning a filter for each column and making a ControlWrapper for it
                    controlWrappers.push(new google.visualization.ControlWrapper({
                        controlType: filterType,
                        containerId: self.divIds[i],
                        options: {
                            filterColumnIndex: i
                        }
                    }));
                }

                //attempt to restore previous states, if any
                for (let i = 0; i < self.states.length; i++) {
                    if (self.states[i]) {
                        controlWrappers[i].setState(self.states[i]);
                        console.log("restoring state: " + self.states[i].lowValue + " - " + self.states[i].highValue);
                    }
                }

                //grab controlWrapper states when the chart is ready
                for (let i = 0; i < controlWrappers.length; i++) {
                    google.visualization.events.addListener(controlWrappers[i], 'ready', () => {
                        console.log("-----")
                        console.log("lowVal: " + controlWrappers[i].getState().lowValue);
                        console.log("hiVal : " + controlWrappers[i].getState().highValue);
                        self.states[i] = controlWrappers[i].getState();
                    });            
                }

                //for each control wrapper (filter), if the state has changed, get and store its state
                for (let i = 0; i < controlWrappers.length; i++) {
                    google.visualization.events.addListener(controlWrappers[i], 'statechange', () => {
                        self.states[i] = controlWrappers[i].getState();
                    });               
                }

                //bind all the control wrappers to the dashboard
                for (let controlWrapper of controlWrappers) {
                    self.dashboard.bind(controlWrapper, self.wrapper);
                }

                //draw all of the filters/sliders/ControlWrappers in the dashboard
                self.dashboard.draw(chartData);
            }

            self.wrapper.draw(ele);

            /*
            Create a chart editor and listen on the ok and cancel events to change the chart accordingly
            */
            self.chartEditor = new google.visualization.ChartEditor;
            google.visualization.events.addListener(self.chartEditor, 'ok', () => {
                //adjusting the dimensions of the newly drawn chart to fit within the window
                self.wrapper.setOption('height', self.recalcHeight());
                self.wrapper.setOption('width', self.recalcWidth());
                self.chartEditor.getChartWrapper().setOption('height', self.recalcHeight());
                self.chartEditor.getChartWrapper().setOption('width', self.recalcWidth());

                //update the current wrapper to reflect the newly edited chart
                self.wrapper = self.chartEditor.getChartWrapper();
                self.chartOptions = self.wrapper.getOptions();
                self.chartType = self.wrapper.getChartType();

                //redraw chart and make sure to recreate the PNG image
                self.drawGraph(self.chartOptions, self.chartType, self.chartData, self._element);
                self.isPngCreated = false;
            });

            google.visualization.events.addListener(self.chartEditor, 'cancel', () => {
                //adjust dimensions of the original chart on cancel
                self.wrapper.setOption('height', self.recalcHeight());
                self.wrapper.setOption('width', self.recalcWidth());
            });

            google.visualization.events.addListener(self.wrapper, 'select', function () {
                const selectedItem = self.wrapper.getChart().getSelection()[0];
                if (selectedItem) {
                    let msg;
                    if (selectedItem !== undefined) {
                        const selectedRowValues = [];
                        if (selectedItem.row !== null) {
                            selectedRowValues.push(self.wrapper.getDataTable().getValue(selectedItem.row, 0));
                            selectedRowValues.push(self.wrapper.getDataTable().getValue(selectedItem.row, selectedItem.column));
                            msg = {
                                message: 'select',
                                row: selectedItem.row,
                                column: selectedItem.column,
                                selectedRowValues: selectedRowValues
                            };
                        }
                    }
                    self.itemSelect.emit(msg);
                } else
                    self.itemDeselect.emit();
            });    

        }

    }

    drawEditor() {
        this.chartEditor.getChartWrapper().draw(this._element);
        this.redrawChartEditor();
    }

    redrawChartEditor() {
        this.chartEditor.setOption('height', this.recalcHeight());
        this.chartEditor.setOption('width', this.recalcWidth());
    }

    recalcHeight() {
        return '300px';
    }

    recalcWidth() {
        return Math.min(document.documentElement.clientWidth, window.innerWidth || 0) + 'px';
    }

    openEditor() {
        this.chartEditor.openDialog(this.wrapper, {});   
    }

    //assumes that the chart isn't a table chart
    getPNG(): Blob {
            let img: string = this.wrapper.getChart().getImageURI();
            let base64: string = img.replace(/^data:image\/\w+;base64,/, "");

            let blob: Blob = this.b64toBlob(base64, 'img/png', 512);
        return blob;
    }

    //method to convert a base-64 png image to a Blob for use with FileSaver.js
    b64toBlob(b64Data, contentType, sliceSize) {
        contentType = contentType || '';
        sliceSize = sliceSize || 512;
      
        var byteCharacters = atob(b64Data);
        var byteArrays = [];
      
        for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
          var slice = byteCharacters.slice(offset, offset + sliceSize);
      
          var byteNumbers = new Array(slice.length);
          for (var i = 0; i < slice.length; i++) {
            byteNumbers[i] = slice.charCodeAt(i);
          }
      
          var byteArray = new Uint8Array(byteNumbers);
      
          byteArrays.push(byteArray);
        }
      
        var blob = new Blob(byteArrays, {type: contentType});
        return blob;
    }
}
