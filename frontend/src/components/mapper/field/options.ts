import {
  MappingConfigSpec,
  ValueSpecInteger,
  ValueSpecString,
  ValueSpecTransactionTypeProperty,
} from "@/api";
import { ISelectOption } from "@/typings/ISelectOption";

export type MappingConfigSpecFieldName = keyof MappingConfigSpec;
type MappingConfigSpecFieldValue =
  | ValueSpecTransactionTypeProperty
  | ValueSpecString
  | ValueSpecInteger
  | boolean;

type MappingConfigSpecInputType =
  | "boolean"
  | "transaction-type"
  | "string"
  | "integer"
  | "string-date";

type MappingConfigSpecInputConfig = {
  type: MappingConfigSpecInputType;
  label: string;
  help?: string;
  required?: boolean;
};

type MappingConfigSpecFieldsMap = {
  [Property in MappingConfigSpecFieldName]: MappingConfigSpecInputConfig;
};

const MAPPING_CONFIG_FIELDS: MappingConfigSpecFieldsMap = {
  applyRules: { type: "boolean", label: "Apply rules" },
  fireWebhooks: { type: "boolean", label: "Fire webhooks" },
  type: { type: "transaction-type", label: "Transaction type", required: true },
  date: { type: "string-date", label: "Transaction date", required: true },
  amount: { type: "string", label: "Amount", required: true },
  description: { type: "string", label: "Description", required: true },
  currencyId: { type: "integer", label: "Currency ID" },
  currencyCode: { type: "string", label: "Currency Code" },
  foreignAmount: { type: "string", label: "Foreign Amount" },
  foreignCurrencyId: { type: "integer", label: "Foreign Currency ID" },
  foreignCurrencyCode: { type: "string", label: "Foreign Currency Code" },
  budgetId: { type: "integer", label: "Budget ID" },
  categoryId: { type: "integer", label: "Category ID" },
  categoryName: { type: "string", label: "Category Name" },
  sourceId: { type: "integer", label: "Source Account ID" },
  sourceName: { type: "string", label: "Source Account Name" },
  destinationId: { type: "string", label: "Destination Account ID" },
  destinationName: { type: "string", label: "Destination Account Name" },
  piggyBankId: { type: "integer", label: "Piggy Bank ID" },
  piggyBankName: { type: "string", label: "Piggy Bank Name" },
  billId: { type: "integer", label: "Bill ID" },
  billName: { type: "string", label: "Bill Name" },
  tags: { type: "string", label: "Tags" },
  notes: { type: "string", label: "Notes" },
  internalReference: { type: "string", label: "Internal Reference/ID" },
  externalId: { type: "string", label: "External ID" },
  externalUrl: { type: "string", label: "External URL" },
  bunqPaymentId: { type: "integer", label: "Internal ID of bunq transaction" },
  sepaCc: { type: "string", label: "SEPA Clearing Code" },
  sepaCtOp: { type: "string", label: "SEPA Opposing Account Identifier" },
  sepaCtId: { type: "string", label: "SEPA end-to-end Identifier" },
  sepaDb: { type: "string", label: "SEPA mandate identifier" },
  sepaCountry: { type: "string", label: "SEPA Country" },
  sepaEp: { type: "string", label: "SEPA External Purpose indicator" },
  sepaCi: { type: "string", label: "SEPA Creditor Identifier" },
  sepaBatchId: { type: "string", label: "SEPA Batch ID" },
  interestDate: { type: "string-date", label: "Interest Date" },
  bookDate: { type: "string-date", label: "Book Date" },
  processDate: { type: "string-date", label: "Process Date" },
  dueDate: { type: "string-date", label: "Due Date" },
  paymentDate: { type: "string-date", label: "Payment Date" },
  invoiceDate: { type: "string-date", label: "Invoice Date" },
};

export const FIREFLY_FIELDS: ISelectOption[] = Object.entries(
  MAPPING_CONFIG_FIELDS
).map(([key, value]) => ({
  id: key,
  label: value.label,
}));

export type FieldConfiguration = {
  inputConfiguration: MappingConfigSpecInputConfig;

  fieldName: MappingConfigSpecFieldName;
  fieldValue: MappingConfigSpecFieldValue;
};

export const DEFAULT_MAPPING_CONFIG: FieldConfiguration[] = [
  {
    inputConfiguration: MAPPING_CONFIG_FIELDS.applyRules,
    fieldName: "applyRules",
    fieldValue: true,
  },
  {
    inputConfiguration: MAPPING_CONFIG_FIELDS.fireWebhooks,
    fieldName: "fireWebhooks",
    fieldValue: true,
  },
  {
    inputConfiguration: MAPPING_CONFIG_FIELDS.type,
    fieldName: "type",
    fieldValue: {
      constant: "transfer",
    } as ValueSpecTransactionTypeProperty,
  },
  {
    inputConfiguration: MAPPING_CONFIG_FIELDS.date,
    fieldName: "date",
    fieldValue: {} as ValueSpecString,
  },
  {
    inputConfiguration: MAPPING_CONFIG_FIELDS.amount,
    fieldName: "date",
    fieldValue: {} as ValueSpecString,
  },
  {
    inputConfiguration: MAPPING_CONFIG_FIELDS.description,
    fieldName: "date",
    fieldValue: {} as ValueSpecString,
  },
];

export type IFileChooserConfigurationInfo = {
  encoding: ISelectOption;
  delimeter: ISelectOption;
  quoteChar: ISelectOption;
  file: File;
  clearFile: VoidFunction;
};
