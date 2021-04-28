package org.glycoinfo.GlycanFormatConverter.cli;

import org.glycoinfo.GlycanFormatconverter.io.JSON.GCJSONExporter;
import org.glycoinfo.GlycanFormatconverter.io.JSON.GCJSONImporter;
import org.glycoinfo.GlycanFormatconverter.util.ExporterEntrance;
import org.glycoinfo.GlycanFormatconverter.io.IUPAC.IUPACStyleDescriptor;
import org.glycoinfo.GlycanFormatconverter.io.KCF.KCFImporter;
import org.glycoinfo.GlycanFormatconverter.io.IUPAC.extended.IUPACExtendedImporter;
import org.glycoinfo.GlycanFormatconverter.io.IUPAC.condensed.IUPACCondensedImporter;
import org.glycoinfo.GlycanFormatconverter.io.LinearCode.LinearCodeImporter;
import org.glycoinfo.GlycanFormatconverter.io.WURCS.WURCSImporter;
import org.glycoinfo.GlycanFormatconverter.io.GlycoCT.WURCSToGlycoCT;
import org.glycoinfo.WURCSFramework.io.GlycoCT.WURCSExporterGlycoCT;
import org.glycoinfo.GlycanFormatconverter.Glycan.GlyContainer;

public class ConverterPortal {

    private InputFormat inputFormat;
    private OutputFormat outputFormat;
    private String inputSequence;
    private String outputSequence;
    private String inputPath;
    private String outputPath;

    public ConverterPortal () {
        this.inputSequence = "";
        this.outputSequence = "";
        this.inputPath = "";
        this.outputPath = "";
    }

    /* Util template
    public ConverterPortal () {
        return this;
    }
     */

    public ConverterPortal inputSequence (String _inputSequence) {
        this.inputSequence = _inputSequence;
        return this;
    }

    public ConverterPortal inputFormat (InputFormat _inputFormat) {
        this.inputFormat = _inputFormat;
        return this;
    }

    public ConverterPortal outputFormat (OutputFormat _outputFormat) {
        this.outputFormat = _outputFormat;
        return this;
    }

    public ConverterPortal inputPath (String _inputPath) {
        this.inputPath = _inputPath;
        return this;
    }

    public ConverterPortal outputPath (String _outputPath) {
        this.outputPath = _outputPath;
        return this;
    }

    public void start () {
        try {
            GlyContainer gc = null;

            if (this.inputFormat != InputFormat.WURCS && this.outputFormat == OutputFormat.JSON) {
                //TODO: 途中でWURCSヘの変換を挟むか
                throw new Exception ("WURCSJSON can not be obtained from a non-WURCS format.");
            }

            // define importer and glycontainer
            if (this.inputFormat == InputFormat.KCF) {
                KCFImporter ki = new KCFImporter();
                gc = ki.start(this.inputSequence);
            } else if (this.inputFormat == InputFormat.IUPAC_CONDENSED) {
                IUPACCondensedImporter ici = new IUPACCondensedImporter();
                gc = ici.start(this.inputSequence);
            } else if (this.inputFormat == InputFormat.IUPAC_EXTENDED) {
                IUPACExtendedImporter iei = new IUPACExtendedImporter();
                gc = iei.start(this.inputSequence);
            } else if (this.inputFormat == InputFormat.GLYCOCT) { //only GlycoCT2WURCS
                if (this.outputFormat == OutputFormat.WURCS) {
                    WURCSExporterGlycoCT w2gct = new WURCSExporterGlycoCT();
                    w2gct.start(this.inputSequence);
                    this.outputSequence = w2gct.getWURCS();
                } else {
                    throw new Exception("In GlycanFormatConverter, GlycoCT is not support to be utilized for other than WURCS format conversion.");
                }
            } else if (this.inputFormat == InputFormat.LINEARCODE) {
                LinearCodeImporter lci = new LinearCodeImporter();
                gc = lci.start(this.inputSequence);
            } else if (this.inputFormat == InputFormat.WURCS) {
                if (this.outputFormat == OutputFormat.GLYCOCT) {// WURCS to GlycoCT
                    WURCSToGlycoCT w2gct = new WURCSToGlycoCT();
                    w2gct.start(this.inputSequence);
                    this.outputSequence = w2gct.getGlycoCT();
                } else if (this.outputFormat == OutputFormat.JSON) {// WURCS to WURCSJSON
                    WURCSImporter wi = new WURCSImporter();
                    wi.start(this.inputSequence);
                    GCJSONExporter jsonEx = new GCJSONExporter();
                    this.outputSequence = jsonEx.start(wi.getConverter());
                } else {
                    WURCSImporter wi = new WURCSImporter();
                    gc = wi.start(this.inputSequence);
                }
            } else if (this.inputFormat == InputFormat.JSON) {
                GCJSONImporter jsonIm = new GCJSONImporter();
                gc = jsonIm.start(this.inputSequence);
            }

            if (gc == null) return;

            ExporterEntrance ee = new ExporterEntrance(gc);

            // define exporter and output sequence
            if (this.outputFormat == OutputFormat.IUPAC_CONDENSED) {
                this.outputSequence = ee.toIUPAC(IUPACStyleDescriptor.CONDENSED);
            } else if (this.outputFormat == OutputFormat.IUPAC_EXTENDED) {
                this.outputSequence = ee.toIUPAC(IUPACStyleDescriptor.GREEK);
            } else if (this.outputFormat == OutputFormat.IUPAC_SHORT) {
                this.outputSequence = ee.toIUPAC(IUPACStyleDescriptor.SHORT);
            } else if (this.outputFormat == OutputFormat.GLYCANWEB) {
                System.err.println("glycan web");
                this.outputSequence = ee.toIUPAC(IUPACStyleDescriptor.GLYCANWEB);
                System.err.println(this.outputSequence);
            } else if (this.outputFormat == OutputFormat.WURCS) {
                this.outputSequence = ee.toWURCS();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public String getOutputSequence () {
        return this.outputSequence;
    }

    //TODO: inputとoutputのFileを扱う関数の実装


}