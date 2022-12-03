import { RECORD_SEP, UNIT_SEP } from "papaparse";

export const ENCODINGS: ReadonlyArray<ISelectOption> = [
  "UTF-8",
  "ISO-8859-1",
  "WINDOWS-1251",
  "KOI8-R",
  "KOI8-U",
  "KOI-7",
  "ISO-8859-5",
  "CP855",
  "CP866",
  "MACCYRILLIC",
  "MIK",
  "GB2312",
  "CP949",
  "CP1361",
  "CP930",
  "CP933",
  "CP1166",
  "UTF-7",
  "UTF-16",
  "SJIS",
  "ISO-2022-JP",
  "BIG-5",
  "BDSKBD",
  "IBM-1025",
].map(mapEncoding);

export type ISelectOption = {
  readonly id: string;
  readonly label: string;
};

export function mapEncoding(item: string): ISelectOption {
  return { id: item, label: item };
}

export const DELIMETERS: ISelectOption[] = [
  // { id: '"', label: 'Double Quote (")' },
  // { id: "'", label: "Single Quote (')" },
  { id: ",", label: "Comma (,)" },
  { id: "\t", label: "Tab (\\t)" },
  { id: "|", label: "Pipe (|)" },
  { id: ";", label: "Semicolon (;)" },
  { id: RECORD_SEP, label: "Record Sep (ASCII #30)" },
  { id: UNIT_SEP, label: "Unit Sep (ASCII #31)" },
];

export const QUOTE_CHARS: ISelectOption[] = [
  { id: '"', label: 'Double Quote (")' },
  { id: "'", label: "Single Quote (')" },
];
